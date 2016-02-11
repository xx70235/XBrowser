package model;

public class OfferDao {
    private Integer id;

    private String name;

    private String url;

    private String company;

    private Float commision;

    private Byte saleRate;

    private Integer impression;

    private Integer click;

    private Integer executeTime;

    private Integer successTime;

    private Integer failedTime;

    private String description;

    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public Float getCommision() {
        return commision;
    }

    public void setCommision(Float commision) {
        this.commision = commision;
    }

    public Byte getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Byte saleRate) {
        this.saleRate = saleRate;
    }

    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Integer successTime) {
        this.successTime = successTime;
    }

    public Integer getFailedTime() {
        return failedTime;
    }

    public void setFailedTime(Integer failedTime) {
        this.failedTime = failedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }
}