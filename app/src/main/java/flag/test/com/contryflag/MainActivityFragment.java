package flag.test.com.contryflag;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import flag.test.com.contryflag.model.Country;

public class MainActivityFragment extends Fragment {

    private static final String JSON_ANSWER = "answer.json";
    private static final String AFRICA_REGION = "Africa";
    private static final String REGION_CAPITAL = "capital";
    private static final String REGION_PHOTO = "photo";

    private ImageView countryFlagImg;
    private InputStream stream;

    List<String> randRegionsIndex;
    private List<Country> listAnswers;
    private List<String> listRandRegions;
    private Map<Integer, String> regions;


    private static final String TAG = "MainActivityFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regions  = new HashMap<>();
            regions.put(1, "Africa");
            regions.put(2, "Asia");
            regions.put(3, "Europe");
            regions.put(4, "North_America");
            regions.put(5, "South_America");
        
        //List<Integer> randRegionsIndex = new ArrayList<>(regions.keySet());
        randRegionsIndex = new ArrayList<>();
        randRegionsIndex.add("Africa");
        randRegionsIndex.add("Asia");
        randRegionsIndex.add("Europe");
        randRegionsIndex.add("North_America");
        randRegionsIndex.add("South_America");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        countryFlagImg = view.findViewById(R.id.contry_flag);

        init();

        return view;
    }

    private void init(){
        getAnswers();
        try {
            //Country answer = setRandRegion();
            //String answer = setRandRegion().get(0);
            //Log.d(TAG, "init: " + answer + "/" + answer + ".png");
            stream = getContext().getAssets()
                    .open( "Africa" + "/" + "togo" + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            countryFlagImg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if( stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


    }

    private String loadJSONFromAsset() {
        String json = null;
        AssetManager manager = getActivity().getAssets();

        try {
            stream = manager.open(JSON_ANSWER);
            byte[] answerArray = new byte[stream.available()];
            stream.read(answerArray);

            json = new String(answerArray, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if( stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return json;
        }
    }

    private void getAnswers() {
        listRandRegions = setRandRegion();
        //setRandRegion();
        Log.d(TAG, "getAnswers: +++++++++++++ " + listRandRegions.get(0));
        listAnswers = new ArrayList<>();
        try {
            JSONObject objectAnswer = new JSONObject(loadJSONFromAsset());
            Iterator<String> firstNode = objectAnswer.keys();

            while (firstNode.hasNext()){
                String firstNodeKeys = firstNode.next();
                if (firstNodeKeys.equals(listRandRegions.get(0))) {
                    JSONObject firstNodeValue = objectAnswer.getJSONObject(firstNodeKeys);
                    Iterator<String> SecondNode = firstNodeValue.keys();

                    while (SecondNode.hasNext()) {
                        String SecondNodeKeys = SecondNode.next();
                        JSONObject SecondNodeValue = firstNodeValue.getJSONObject(SecondNodeKeys);
                        String capital = (String) SecondNodeValue.get(REGION_CAPITAL);
                        String photo = (String) SecondNodeValue.get(REGION_PHOTO);

                        listAnswers.add(new Country(AFRICA_REGION, SecondNodeKeys, capital, photo));
                    }
                }
            }
            Log.d(TAG, "getAnswers: " + listAnswers.get(0).getCountry());
            Log.d(TAG, "getAnswers: " + listAnswers.get(1).getCountry());
            Log.d(TAG, "getAnswers: " + listAnswers.get(2).getCountry());
            Log.d(TAG, "getAnswers: " + listAnswers.get(3).getCountry());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<String> setRandRegion(){
        List<String> countries = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int indexRegions =  new Random().nextInt(regions.size());
            countries.add(randRegionsIndex.get(indexRegions));
        }
        Log.d(TAG, "setRandRegion: "+ countries.get(0) + countries.get(1)+countries.get(2)+countries.get(3));
        return countries;
    }

    private  void setRandContries(){
        for (int i = 0; i < listAnswers.size(); i++) {
            int index =  new Random().nextInt(listAnswers.size());
        }

    }

}
