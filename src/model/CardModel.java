package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import utils.HttpRequestTool;

public class CardModel {
	private String cardnum;

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public CardStatus getcStatus() {
		return cStatus;
	}

	public void setcState(CardStatus cState) {
		this.cStatus = cState;
	}

	private String month;
	private String year;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setcStatus(CardStatus cStatus) {
		this.cStatus = cStatus;
	}

	private String cvv;
	private String name;
	private String fname;
	private String lname;
	private String accName;
	private String email;
	private String addr;
	private String city;
	private String state;
	private String zipcode;
	private String telnum;
	private String country;
	private String ssn;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	// private HashMap<String, String> stateMap;
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;
	private CardStatus cStatus;

	// enum CardState
	// {
	// Normal, LowBalance, WrongAddress, Dead, Unknown
	// }

	public void generateModel(String line) {
		// CardModel model = new CardModel();
		// generateStateTable();
		String[] tmps = line.split("\\|");
		cardnum = tmps[0];
		month = tmps[1];
		year = tmps[2];
		if(year.length()!=4)
		{
			year="20"+year;
		}
		cvv = tmps[3];
		name = tmps[4];
		geneNameEmail(this);
		addr = tmps[5];
		city = tmps[6];
		state = tmps[7];
		zipcode = tmps[8];
		// country = tmps[9];
		telnum = tmps[9];
		generatePassword(8);
		if (tmps.length < 12) {
			cStatus = CardStatus.Unknown;
		} else {
			cStatus = CardStatus.valueOf(tmps[11]);

		}
//		getFakePersonalInfo();
	}

	private void geneNameEmail(CardModel model) {
		String[] tmps = model.name.split(" ");
		if (tmps.length == 2) {
			model.fname = tmps[0];
			model.lname = tmps[1];
		} else if (tmps.length == 1) {
			model.fname = tmps[0];
			model.lname = "Tomansy";
		} else if (tmps.length >= 3) {
			model.fname = tmps[0];
			model.lname = tmps[2];
		}

		Random r = new Random();
		int codeNum = r.nextInt(4);
		String[] backUpName = new String[2];
		backUpName[0] = model.fname.substring(0, 1).toUpperCase() + model.lname;
		backUpName[1] = model.fname
				+ Integer.parseInt(getRandomNumber(codeNum, r));

		model.accName = backUpName[r.nextInt(2)];
		String[] emailSuffix = { "@msn.com", "@gmail.com", "@live.com",
				"@aol.com", "@yahoo.com" };
		model.email = model.accName + emailSuffix[r.nextInt(5)];

		System.out.println(model.fname);
		System.out.println(model.lname);
		System.out.println(model.accName);
		System.out.println(model.email);

	}

	public String toString() {

		String cardinfo = "Username:" + this.getAccName() + "\r\n"
				+ "First Name: " + this.getFname() + "\r\n" + "Last Name: "
				+ this.getLname() + "\r\n" + "Email: " + this.getEmail()
				+ "\r\n" + "Phone: " + this.getTelnum() + "\r\n"
				+ "Card Number: " + this.getCardnum() + "\r\n" + "Card CVV: "
				+ this.getCvv() + "\r\n" + "Expired Year: " + this.getYear()
				+ "\r\n" + "Expired Month: " + this.getMonth() + "\r\n"
				+ "Address: " + this.getAddr() + "\r\n" + "City: "
				+ this.getCity() + "\r\n" + "State: " + this.getState()
				+ "\r\n" + "zipCode: " + this.getZipcode() + "\r\n"
				+ "Password: " + this.getPassword() + "\r\n";

		return cardinfo;

	}

	private void generatePassword(int pwd_len) {
		// 35����Ϊ�����Ǵ�0��ʼ�ģ�26����ĸ+10������
		int maxNum = 36;
		int i; // ��ɵ������
		int count = 0; // ��ɵ�����ĳ���
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// ��������ȡ���ֵ����ֹ��ɸ���

			i = Math.abs(r.nextInt(maxNum)); // ��ɵ������Ϊ36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
char t = Character.toUpperCase(str[r.nextInt(26)]);
		this.password =t+ pwd.toString();
	}

	public String getRandomNumber(int digCount, Random r) {
		digCount++;
		StringBuilder sb = new StringBuilder(digCount);
		for (int i = 0; i < digCount; i++)
			sb.append((char) ('0' + r.nextInt(10)));
		return sb.toString();
	}

	public void printUsedCard() {

	}

	public void getFakePersonalInfo() {
		String url = "http://cn.usinfo.me/by-condition/gender/male/age/random";
		StateMap sm = StateMap.getinstance();
		// String[] genders = {"male","female"};
		// String[] age = {"middle_age","old_age"};
		if (state.length() == 2) {
			url += "/state/" + this.state + "/city/"
					+ this.city.replace(" ", "+") + "/zip/" + this.zipcode
					+ "/";
		} else {
			url += "/state/" + sm.getStateMap().get(this.state) + "/city/"
					+ this.city.replace(" ", "+") + "/zip/" + this.zipcode
					+ "/";
		}

		String response = HttpRequestTool.getMethod(url);
		ssn = getToken(response, "id=\"ssn\"", ">");
	}

	// TODO:远程取没取到内容
	private String getToken(String targetString, String startToken,
			String endToken) {
		int startTokenIndex = targetString.indexOf(startToken);
		int endTokenIndex = targetString.indexOf(endToken, startTokenIndex);
		String tmp = targetString.substring(startTokenIndex, endTokenIndex);
		int si = tmp.indexOf("value=\"") + 7;
		int ei = tmp.indexOf("\"", si);
		String target = tmp.substring(si, ei);
		// tmp.substring(beginIndex, endIndex)

		return target;

	}

}

class StateMap {
	private static StateMap sm = null;
	private HashMap<String, String> stateMap;

	public HashMap<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(HashMap<String, String> stateMap) {
		this.stateMap = stateMap;
	}

	private StateMap() {
		stateMap = new HashMap<String, String>();
		generateStateTable();
	}

	public static StateMap getinstance() {
		if (sm == null)
			sm = new StateMap();
		return sm;
	}

	private void generateStateTable() {
		stateMap = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("state.txt")));
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				String[] tmps = tmp.split(" ");
				stateMap.put(tmps[0], tmps[1]);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}

	}
}
