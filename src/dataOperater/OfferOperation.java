package dataOperater;


import dao.OfferDaoMapper;
import model.OfferDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.List;

/**
 * Created by xuyunfei on 16/1/21.
 */
public class OfferOperation {
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static{
        try{
        reader    = Resources.getResourceAsReader("Configuration.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }catch(Exception e){
        e.printStackTrace();
    }
    }

    public static void getOfferByName(String name)
    {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            OfferDaoMapper offerOperation = session.getMapper(OfferDaoMapper.class);
            List<OfferDao> offerList = offerOperation.selectByOfferName(name);
            for(OfferDao offer : offerList)
            {
                System.out.println(offer.getId());
                System.out.println(offer.getName());
                System.out.println(offer.getUrl());
            }

        }
        finally
        {
            session.close();
        }
    }
    
    public static List<OfferDao> getAllOffers()
    {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            OfferDaoMapper offerOperation = session.getMapper(OfferDaoMapper.class);
            List<OfferDao> offerList = offerOperation.selectAllOffers();
            for(OfferDao offer : offerList)
            {
                System.out.println(offer.getId());
                System.out.println(offer.getName());
                System.out.println(offer.getUrl());
            }
            return offerList;

        }
        finally
        {
            session.close();
        }
    }

    public static void main(String[] args)
    {
//        SqlSession session = sqlSessionFactory.openSession();
//        try
//        {
//            OfferDaoMapper offerOperation = session.getMapper(OfferDaoMapper.class);
//            OfferDao offer = offerOperation.selectByOfferName(offerName)(3539);
//            System.out.println(offer.getName());
//            System.out.println(offer.getUrl());
//
//
//
//        }
//        finally {
//            session.close();
//        }

//        getOfferByName("*");
    	getAllOffers();
    }
}


