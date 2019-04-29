package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Category> findAll() {

        //1.先从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //1.1 期望获取的数据有顺序
        Set<String> categorys = jedis.zrange("category", 0, -1);

        //2.判断查询的集合是否为null
        List<Category> cs = null;
        if (categorys == null || categorys.size() == 0){
            //如果为空:第一次访问,需要查询数据库,将数据存入redis
            cs = categoryDao.findAll();
            //期望获取的数据有顺序
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }

        }else {
            //如果不为空:不是第一次访问,将Set中的数据存入List
            cs = new ArrayList<Category>();
            for (String c:categorys) {
                Category category = new Category();
                category.setCname(c);
                cs.add(category);
            }
        }
        return cs;
    }
}
