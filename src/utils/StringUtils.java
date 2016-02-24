package utils;

import java.util.Random;

import model.ProfileDao;

public class StringUtils {

	public static String generateUsername(ProfileDao profileDao)
	{
		Random r = new Random();
		String[] username =new String[3];
		username[0] = profileDao.getFirstName()+"_"+profileDao.getLastName()+getRandomNumber(r.nextInt(3));
		username[1] = profileDao.getLastName().substring(0, 1).toUpperCase()+profileDao.getFirstName()+getRandomNumber(r.nextInt(3));
		username[3] = profileDao.getFirstName()+profileDao.getLastName().substring(0, 1).toUpperCase()+getRandomNumber(r.nextInt(3));
		
		return username[r.nextInt(3)];
	}
	
	public static String generatePassword(int pwd_len) {
		int maxNum = 36;
		int i; 
		int count = 0; 
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			

			i = Math.abs(r.nextInt(maxNum)); 

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
         char t = Character.toUpperCase(str[r.nextInt(26)]);
		 String password = t+ pwd.toString()+getRandomNumber(2);
		 return password;
		 
	}

	public static String getRandomNumber(int digCount) {
		Random r  = new Random();
		digCount++;
		StringBuilder sb = new StringBuilder(digCount);
		for (int i = 0; i < digCount; i++)
			sb.append((char) ('0' + r.nextInt(10)));
		return sb.toString();
	}
}
