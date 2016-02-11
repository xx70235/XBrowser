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
import java.util.List;
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

	protected boolean postData(List<NameValuePair> nvps, String url) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();

			if (cookies.isEmpty()) {
				System.out.println("None");
				return false;
			} else {

				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	protected String selectADType(String ADFilePath, String adressFilePath) {
		String responseString = "none";
		// 广告页面标签地址
		List<String> ADTypePair = new ArrayList<String>();
		// 美国城市及州地址
		List<String> addressPair = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(ADFilePath)));
			BufferedReader bt = new BufferedReader(new FileReader(new File(adressFilePath)));
			String line;
			String line2;
			while ((line = br.readLine()) != null) {
				ADTypePair.add(line);
			}
			while ((line2 = bt.readLine()) != null) {
				addressPair.add(line2);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String typeName = ADTypePair.get(0);
		String typeUrl = ADTypePair.get(1);
		String tableUrl = ADTypePair.get(2);
		String cityKey = addressPair.get(0);
		String stateKey = addressPair.get(1);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("cityKey", cityKey));
		nvps.add(new BasicNameValuePair("stateKey", stateKey));

		try {
			HttpPost httppost = new HttpPost(typeUrl);
			httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = httpClient.execute(httppost);
			// 销毁连接
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			// 请求表格页面
			HttpPost httppost1 = new HttpPost(tableUrl);
			HttpResponse response1 = httpClient.execute(httppost1);
			HttpEntity entity1 = response1.getEntity();
			// 得到表格页面
			responseString = EntityUtils.toString(entity1, "GBK");
			EntityUtils.consume(entity1);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}

	public ProfileDao getProfileFromWeb(String category, String proxyState, String proxyCity) {
		ProfileDao profileDao = null;
		boolean isLoginOK = login("login.txt");
		if (isLoginOK) {
			// 进资料页面
			String tableString = selectADType("ADType.txt", "adress.txt");
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
		ProfileDao profileDao = operation.getProfileFromWeb("Insurance", "NY", "New york");
		System.out.println(profileDao.getPhone());
	}

}
