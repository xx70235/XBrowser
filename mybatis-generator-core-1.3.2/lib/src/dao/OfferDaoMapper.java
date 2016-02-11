package dao;

import model.OfferDao;

public interface OfferDaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferDao record);

    int insertSelective(OfferDao record);

    OfferDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferDao record);

    int updateByPrimaryKey(OfferDao record);
}