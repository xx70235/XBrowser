package dataOperater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import dao.ProfileDaoMapper;
import model.ProfileDao;
import utils.WebOperation;

public class WebProfileOperation extends WebOperation {

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	static {
		try {
			reader = Resources.getResourceAsReader("Configuration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	protected String webProfileQuery(String category, String state, String city) {
		String queryUrl = generateQueryUrl(category);
		String resultUrl = generateResultUrl(category);
		String cityKey = city;
		String stateKey = state;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("cityKey", cityKey));
		nvps.add(new BasicNameValuePair("stateKey", stateKey));

		String queryPageResult = getInfo(queryUrl);
		String queryResult = postData(nvps, resultUrl,"GBK");

		return queryResult;
	}

	/**
	 * 进入到资料页面，根据广告类别、proxy的州和城市进行资料的获取，将所有获取的资料存入本地数据库，并随机返回一条资料作为当前资料使用
	 * @param category 
	 * @param proxyState
	 * @param proxyCity
	 * @return
	 */
	public ProfileDao getProfileFromWeb(String category, String proxyState, String proxyCity) {
		ProfileDao profileDao = null;
		boolean isLoginOK = login("login.txt");
		if (isLoginOK) {
			// 进资料页面
			String tableString = webProfileQuery(category,proxyState,proxyCity);
			System.out.println(tableString);
			// 取资料下来并存入数据库
			Document doc = Jsoup.parse(tableString);
			Elements trs = doc.select("table").select("tr");
			Random random = new Random();
			int r = random.nextInt(trs.size());
			if(r==0)
				r=1;
			//i从1开始，忽略表头
			for (int i = 1; i <trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				ArrayList<String> para = new ArrayList<String>();
				for (int j = 0; j < tds.size(); j++) {
					String text = tds.get(j).text();
					para.add(text);
					System.out.println(text);
				}
				ProfileDao tmp = addProfile2DB(para.get(0), para.get(1), para.get(2), para.get(3), para.get(4),
						para.get(5), para.get(6), para.get(7), para.get(8), para.get(9), category);
				//从列表中随机返回一个profile供当前广告使用。
				if (r == i) {
					profileDao = tmp;
				}
			}
		}
		return profileDao;
	}
	
	private String generateQueryUrl(String category)
	{
		if(category.contains(" "))
			category = category.replace(" ", "_").toLowerCase();
		String url ="";
		//Cash_Loans
		if(category.contains("cash"))
		{
			url = "http://italku.com/search_loans.asp";
		}
		//Shopping_Coupons
		else if(category.contains("shopping"))
		{
			url = "http://italku.com/search_shopping.asp";
		}
		else if(category.contains("credit"))
		{
			url = "http://italku.com/search_credit.asp";
		}
		else {
			url = "http://italku.com/search_"+category+".asp";
		}
		 return url;
	}
	
	private String generateResultUrl(String category)
	{
		if(category.contains(" "))
			category = category.replace(" ", "_").toLowerCase();
		String url ="";
		//Cash_Loans
		if(category.contains("cash"))
		{
			url = "http://italku.com/loans.asp";
		}
		//Shopping_Coupons
		else if(category.contains("shopping"))
		{
			url = "http://italku.com/shopping.asp";
		}
		else if(category.contains("credit"))
		{
			url = "http://italku.com/credit.asp";
		}
		else {
			url = "http://italku.com/"+category+".asp";
		}
		 return url;
	}

	/**
	 * 将从网站上获取的资料存入数据库
	 * @param Email
	 * @param FirstName
	 * @param LastName
	 * @param Gender
	 * @param Adress
	 * @param City
	 * @param State
	 * @param Postcode
	 * @param PhoneNumber
	 * @param SetLengend
	 * @param category
	 * @return
	 */
	private ProfileDao addProfile2DB(String Email, String FirstName, String LastName, String Gender, String Adress,
			String City, String State, String Postcode, String PhoneNumber, String SetLengend, String category) {

		ProfileDao profile = new ProfileDao();
		profile.setPhone(PhoneNumber);
		profile.setEmail(Email);
		profile.setFirstName(FirstName);
		profile.setLastName(LastName);
		profile.setGender(Gender);
		profile.setCity(City);
		profile.setState(State);
		profile.setZipcode(Postcode);
		profile.setCatetory(category);
		// profile.setSetLengend(SetLengend);
		profile.setAddress(Adress);

		SqlSession session = sqlSessionFactory.openSession();
		try {
			ProfileDaoMapper profileOperation = session.getMapper(ProfileDaoMapper.class);
			profileOperation.insert(profile);
			session.commit();
			System.out.println("当前增加的用户电话为" + profile.getPhone());
		} finally {
			session.close();
		}

		return profile;
	}

	public static void main(String[] args) {

		// 表格页面
		String tableString;

		WebProfileOperation operation = new WebProfileOperation();
		ProfileDao profileDao = operation.getProfileFromWeb("Auto Insurance", "NY", "New york");
		System.out.println(profileDao.getPhone());
	}

}
