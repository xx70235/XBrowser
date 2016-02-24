package model;

import java.util.HashMap;
import java.util.Map;



public class StateNameMap {
	public static final Map<String, String> states;
	static{
		states = new HashMap<String, String>();
		states.put("alabama","AL");
		states.put("alaska","AK");
		states.put("alberta","AB");
		states.put("american Samoa","AS");
		states.put("arizona","AZ");
		states.put("arkansas","AR");
		states.put("armed forces (ae)","AE");
		states.put("armed forces americas","AA");
		states.put("armed forces pacific","AP");
		states.put("british columbia","BC");
		states.put("california","CA");
		states.put("colorado","CO");
		states.put("connecticut","CT");
		states.put("delaware","DE");
		states.put("district ff columbia","DC");
		states.put("florida","FL");
		states.put("georgia","GA");
		states.put("guam","GU");
		states.put("hawaii","HI");
		states.put("idaho","ID");
		states.put("illinois","IL");
		states.put("indiana","IN");
		states.put("iowa","IA");
		states.put("kansas","KS");
		states.put("kentucky","KY");
		states.put("louisiana","LA");
		states.put("maine","ME");
		states.put("manitoba","MB");
		states.put("maryland","MD");
		states.put("massachusetts","MA");
		states.put("michigan","MI");
		states.put("minnesota","MN");
		states.put("mississippi","MS");
		states.put("missouri","MO");
		states.put("montana","MT");
		states.put("nebraska","NE");
		states.put("nevada","NV");
		states.put("new brunswick","NB");
		states.put("new hampshire","NH");
		states.put("new jersey","NJ");
		states.put("new mexico","NM");
		states.put("new york","NY");
		states.put("newfoundland","NF");
		states.put("north carolina","NC");
		states.put("north dakota","ND");
		states.put("northwest territories","NT");
		states.put("nova scotia","NS");
		states.put("nunavut","NU");
		states.put("ohio","OH");
		states.put("oklahoma","OK");
		states.put("ontario","ON");
		states.put("oregon","OR");
		states.put("pennsylvania","PA");
		states.put("prince edward island","PE");
		states.put("puerto rico","PR");
		states.put("quebec","QC");
		states.put("rhode island","RI");
		states.put("saskatchewan","SK");
		states.put("south carolina","SC");
		states.put("south dakota","SD");
		states.put("tennessee","TN");
		states.put("texas","TX");
		states.put("utah","UT");
		states.put("vermont","VT");
		states.put("virgin islands","VI");
		states.put("virginia","VA");
		states.put("washington","WA");
		states.put("west virginia","WV");
		states.put("wisconsin","WI");
		states.put("wyoming","WY");
		states.put("yukon territory","YT");
	}
	public static final Map<String, String> STATE_MAP;
	static {
	    STATE_MAP = new HashMap<String, String>();
	    STATE_MAP.put("AL", "Alabama");
	    STATE_MAP.put("AK", "Alaska");
	    STATE_MAP.put("AB", "Alberta");
	    STATE_MAP.put("AZ", "Arizona");
	    STATE_MAP.put("AR", "Arkansas");
	    STATE_MAP.put("BC", "British Columbia");
	    STATE_MAP.put("CA", "California");
	    STATE_MAP.put("CO", "Colorado");
	    STATE_MAP.put("CT", "Connecticut");
	    STATE_MAP.put("DE", "Delaware");
	    STATE_MAP.put("DC", "District Of Columbia");
	    STATE_MAP.put("FL", "Florida");
	    STATE_MAP.put("GA", "Georgia");
	    STATE_MAP.put("GU", "Guam");
	    STATE_MAP.put("HI", "Hawaii");
	    STATE_MAP.put("ID", "Idaho");
	    STATE_MAP.put("IL", "Illinois");
	    STATE_MAP.put("IN", "Indiana");
	    STATE_MAP.put("IA", "Iowa");
	    STATE_MAP.put("KS", "Kansas");
	    STATE_MAP.put("KY", "Kentucky");
	    STATE_MAP.put("LA", "Louisiana");
	    STATE_MAP.put("ME", "Maine");
	    STATE_MAP.put("MB", "Manitoba");
	    STATE_MAP.put("MD", "Maryland");
	    STATE_MAP.put("MA", "Massachusetts");
	    STATE_MAP.put("MI", "Michigan");
	    STATE_MAP.put("MN", "Minnesota");
	    STATE_MAP.put("MS", "Mississippi");
	    STATE_MAP.put("MO", "Missouri");
	    STATE_MAP.put("MT", "Montana");
	    STATE_MAP.put("NE", "Nebraska");
	    STATE_MAP.put("NV", "Nevada");
	    STATE_MAP.put("NB", "New Brunswick");
	    STATE_MAP.put("NH", "New Hampshire");
	    STATE_MAP.put("NJ", "New Jersey");
	    STATE_MAP.put("NM", "New Mexico");
	    STATE_MAP.put("NY", "New York");
	    STATE_MAP.put("NF", "Newfoundland");
	    STATE_MAP.put("NC", "North Carolina");
	    STATE_MAP.put("ND", "North Dakota");
	    STATE_MAP.put("NT", "Northwest Territories");
	    STATE_MAP.put("NS", "Nova Scotia");
	    STATE_MAP.put("NU", "Nunavut");
	    STATE_MAP.put("OH", "Ohio");
	    STATE_MAP.put("OK", "Oklahoma");
	    STATE_MAP.put("ON", "Ontario");
	    STATE_MAP.put("OR", "Oregon");
	    STATE_MAP.put("PA", "Pennsylvania");
	    STATE_MAP.put("PE", "Prince Edward Island");
	    STATE_MAP.put("PR", "Puerto Rico");
	    STATE_MAP.put("QC", "Quebec");
	    STATE_MAP.put("RI", "Rhode Island");
	    STATE_MAP.put("SK", "Saskatchewan");
	    STATE_MAP.put("SC", "South Carolina");
	    STATE_MAP.put("SD", "South Dakota");
	    STATE_MAP.put("TN", "Tennessee");
	    STATE_MAP.put("TX", "Texas");
	    STATE_MAP.put("UT", "Utah");
	    STATE_MAP.put("VT", "Vermont");
	    STATE_MAP.put("VI", "Virgin Islands");
	    STATE_MAP.put("VA", "Virginia");
	    STATE_MAP.put("WA", "Washington");
	    STATE_MAP.put("WV", "West Virginia");
	    STATE_MAP.put("WI", "Wisconsin");
	    STATE_MAP.put("WY", "Wyoming");
	    STATE_MAP.put("YT", "Yukon Territory");
	}
	
	public static String getStateShortName(String fullname)
	{
		return states.get(fullname);
	}
	
	public static String getStateFullName(String shortName)
	{
		return STATE_MAP.get(shortName);
	}

}
