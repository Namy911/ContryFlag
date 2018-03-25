package flag.test.com.contryflag.model;

public class Country {
    private String country;
    private String capital;
    private String region;
    private String photo;

    public Country() {
    }

    public Country(String region, String country, String capital, String photo) {
        this.country = country;
        this.capital = capital;
        this.region = region;
        this.photo = photo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
