package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据sid查询商户
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
