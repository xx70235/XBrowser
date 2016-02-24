package offerOperater;

import java.net.UnknownHostException;

import org.openqa.selenium.WebDriver;

import model.OfferDao;
import model.ProfileDao;
import utils.ProxyDiedException;

public interface OfferOperation {

	public void setUp(WebDriver webDriver);
	public boolean execute(OfferDao offerDao,ProfileDao profileDao) throws UnknownHostException, ProxyDiedException;
}
