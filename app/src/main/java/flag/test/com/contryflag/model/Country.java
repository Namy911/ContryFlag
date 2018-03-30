package flag.test.com.contryflag.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {
    private static final Country ourInstance = new Country();

    private String country;
    private String capital;
    private String region;
    private String photo;

    public Country() {}

    public Country(String region, String country, String capital, String photo) {
        this.country = country;
        this.capital = capital;
        this.region = region;
        this.photo = photo;
    }

    public static Country getInstance() {
        return ourInstance;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.capital);
        dest.writeString(this.region);
        dest.writeString(this.photo);
    }

    protected Country(Parcel in) {
        this.country = in.readString();
        this.capital = in.readString();
        this.region = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}