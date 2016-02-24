package model;

/**
 * DbipLookupDao对象，主要用来返回proxy的国家、州、城市
 * @author B203
 *
 */
public class DbipLookupDao {
    private byte[] ipStart;

    private String addrType;

    private String country;

    private String stateprov;

    private String city;

    private byte[] ipEnd;

    public byte[] getIpStart() {
        return ipStart;
    }

    public void setIpStart(byte[] ipStart) {
        this.ipStart = ipStart;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType == null ? null : addrType.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getStateprov() {
        return stateprov;
    }

    public void setStateprov(String stateprov) {
        this.stateprov = stateprov == null ? null : stateprov.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public byte[] getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(byte[] ipEnd) {
        this.ipEnd = ipEnd;
    }
}