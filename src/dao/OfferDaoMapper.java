package dao;

import model.OfferDao;

import java.util.List;

public interface OfferDaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferDao record);

    int insertSelective(OfferDao record);

    OfferDao selectByPrimaryKey(Integer id);
    
    List<OfferDao> selectByOfferName(String offerName);
    
    List<OfferDao> selectAllOffers();

    int updateByPrimaryKeySelective(OfferDao record);

    int updateByPrimaryKey(OfferDao record);
}