package main;

import java.net.UnknownHostException;

import model.OfferDao;
import model.ProfileDao;
import offerOperater.OfferOperation;
import utils.ProxyDiedException;

public class OperateLead {
	
	Class<?> offerOperClass;
	OfferOperation offerOperation;
	BrowserOperation browserOperation;
	
	public BrowserOperation getBrowserOperation() {
		return browserOperation;
	}

	public void setBrowserOperation(BrowserOperation browserOperation) {
		this.browserOperation = browserOperation;
	}

	public OperateLead()
	{
		
	}
	
	public boolean execute(OfferDao offerDao,ProfileDao profileDao) throws UnknownHostException, ProxyDiedException{
		
		if(this.browserOperation == null)
			return false;
		try {
			String offerClassName = offerDao.getName().replace(" ", "")+offerDao.getId().toString();
			offerOperClass= Class.forName(offerClassName);
			offerOperation = (OfferOperation)offerOperClass.newInstance();
			offerOperation.setUp(browserOperation.getDriver());
			return offerOperation.execute(offerDao,profileDao);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
		
	}

}
