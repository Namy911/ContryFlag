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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import flag.test.com.contryflag.model.Country;

public class MainActivityFragment extends Fragment {

    private static final String JSON_ANSWER = "answer.json";
    private static final String REGION_CAPITAL = "capital";
    private static final String REGION_PHOTO = "photo";

    private ImageView countryFlagImg;
    private InputStream stream;
    private TextView txtRegion;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private Bitmap bitmapFlag;

    private List<String> randRegionsIndex;
    private List<Country> listAnswers;
    private List<Country> countryList;
    private String region;
    private int index;
    //private int[] indexAnswers ;
    private List<String> listRandRegions;
    private Map<Integer, String> regions;


    private static final String TAG = "MainActivityFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAnswers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        countryFlagImg = view.findViewById(R.id.contry_flag);
        txtRegion = view.findViewById(R.id.txt_region);
        answer1 = view.findViewById(R.id.answer1);
        answer2 = view.findViewById(R.id.answer2);
        answer3 = view.findViewById(R.id.answer3);
        answer4 = view.findViewById(R.id.answer4);

        init();
        //setRandAnswers();
        countryFlagImg.setImageBitmap(bitmapFlag);
        txtRegion.setText(region);

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                txtRegion.setText(region);
                countryFlagImg.setImageBitmap(bitmapFlag);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void init(){
        List<Country> listAnswer = setRandAnswers();

        answer1.setText(listAnswer.get(0).getCountry());
        answer2.setText(listAnswer.get(1).getCountry());
        answer3.setText(listAnswer.get(2).getCountry());
        answer4.setText(listAnswer.get(3).getCountry());
        Log.d(TAG, "init: " + listAnswer.get(3).getCountry());

            try {
                region = listAnswer.get(3).getRegion();
                //Log.d(TAG, "init:  " + region + "/" + listAnswer.get(3).getPhoto());
                stream = getContext().getAssets()
                        .open(region + "/" + listAnswer.get(3).getPhoto());
                bitmapFlag = BitmapFactory.decodeStream(stream);

                //removeCountry(indexAnswer);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
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
        countryList = new ArrayList<>();
        try {
            //Country item =  new Country().getInstance();
            JSONObject objectAnswer = new JSONObject(loadJSONFromAsset());
            Iterator<String> firstNode = objectAnswer.keys();

            while (firstNode.hasNext()){
                String firstNodeKeys = firstNode.next();
                    JSONObject firstNodeValue = objectAnswer.getJSONObject(firstNodeKeys);
                    Iterator<String> SecondNode = firstNodeValue.keys();

                    while (SecondNode.hasNext()) {
                        String SecondNodeKeys = SecondNode.next();
                        JSONObject SecondNodeValue = firstNodeValue.getJSONObject(SecondNodeKeys);
                        String capital = (String) SecondNodeValue.get(REGION_CAPITAL);
                        String photo = (String) SecondNodeValue.get(REGION_PHOTO);

                        countryList.add(new Country(firstNodeKeys,SecondNodeKeys,capital,photo));
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Country> setRandAnswers() {
        List<Country> tempList = new ArrayList<>();
        List<Country> tempLite = new ArrayList<>();
        List<Country> tempAnswers = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        tempList.addAll(countryList);
        tempLite.addAll(countryList);
//        if (tempAnswers.size() > 1) {
//            tempAnswers.clear();
//            Log.d(TAG, "setRandAnswers: clear answers");
//        }
        if (tempList.size() > 4) {
            for (int i = 0; i < 4; i++) {
                index.add(new Random().nextInt(tempList.size()));
                tempAnswers.add(tempList.get(index.get(i)));
                tempList.remove(index.get(i));

            }
            removeCountry(tempList.get(index.get(3)));
            Log.d(TAG, "setRandAnswers: tempList  " + tempList.size() + " / " + "setRandAnswers: countryList  " + countryList.size());
            Log.d(TAG, "LIst Index " + index.get(0) + " / " + index.get(1) + " / " + index.get(2) + " / " + index.get(3));
            Log.d(TAG, "--------------------------------------------------------------------");
        } else if (tempList.size() > 1) {
            for (int i = 0; i < 4; i++) {
                index.add(new Random().nextInt(tempLite.size()));
                tempAnswers.add(tempLite.get(index.get(i)));
                tempLite.remove(index.get(i));
            }
            removeCountry(tempLite.get(index.get(3)));
            index.add(new Random().nextInt(tempAnswers.size()));
            Log.d(TAG, "setRandAnswers: tempList  " + tempList.size() + " / " + "setRandAnswers: countryList  " + countryList.size());
            Log.d(TAG, "LIst Index " + index.get(0) + " / " + index.get(1) + " / " + index.get(2) + " / " + index.get(3));
            Log.d(TAG, "--------------------------------------------------------------------");
        } else {
            removeCountry(tempLite.get(0));
            for (int i = 0; i < 3; i++) {
                index.add(new Random().nextInt(countryList.size()));
                tempAnswers.add(countryList.get(index.get(i)));
                countryList.remove(index.get(i));
            }
            tempAnswers.add(tempLite.get(0));

            answer1.setEnabled(false);
            Log.d(TAG, "setRandAnswers: tempList  " + tempList.size() + " / " + "setRandAnswers: countryList  " + countryList.size());
            Log.d(TAG, "LIst Index " + index.get(0) + " / " + index.get(1) + " / " + index.get(2) + " / " + index.get(3));
            Log.d(TAG, "--------------------------------------------------------------------");
        }
        return tempAnswers;
    }

    private void removeCountry(Country country) {
        if (countryList.size() > 1) {
            countryList.remove(country);
            Log.d(TAG, "removeCountry: --------------" + country.getCountry());
        }else {
            Log.d(TAG, "removeCountry: no deleted");
        }
    }

}
