package flag.test.com.contryflag.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Country implements Parcelable {
    static final String TAG = "Country";
    
    private static final Country countryInstance = new Country();
    private ArrayList<Country> list = null;

    private String country;
    private String capital;
    private String region;
    private String photo;

    private Country() {
        list = new ArrayList<Country>();
    }

    public Country(String region, String country, String capital, String photo) {
        this.country = country;
        this.capital = capital;
        this.region = region;
        this.photo = photo;
    }

    public static Country getInstance() {
        return countryInstance;
    }

    public ArrayList<Country> getArray() {
        return this.list;
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

    public List<Country> addToArray(String region, String country, String capital, String photo) {
        Country newCountry = new Country();
        newCountry.setRegion(region);
        newCountry.setCountry(country);
        newCountry.setCapital(capital);
        newCountry.setPhoto(photo);

        list.add(newCountry);
        Log.d(TAG, "addToArray: ");
        return list;
    }

    public void setRandList(){
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            index.add(new Random().nextInt(list.size()));
            Collections.swap(list, index.get(i), i);
        }
        int i = 0;
        Log.d(TAG, "setRandList: " +i);
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