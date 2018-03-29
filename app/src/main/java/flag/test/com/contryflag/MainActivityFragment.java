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

    private List<Country> countryList;
    private List<Country> tempLite = new ArrayList<>();
    private String region;

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

    private void init() {
        List<Country> listAnswer = setRandAnswers();

        if (listAnswer.size() > 1) {
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
        } else {
            Log.d(TAG, "init:  Finish---------------------------------------");
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
        } finally {
            if (stream != null)
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

            while (firstNode.hasNext()) {
                String firstNodeKeys = firstNode.next();
                JSONObject firstNodeValue = objectAnswer.getJSONObject(firstNodeKeys);
                Iterator<String> SecondNode = firstNodeValue.keys();

                while (SecondNode.hasNext()) {
                    String SecondNodeKeys = SecondNode.next();
                    JSONObject SecondNodeValue = firstNodeValue.getJSONObject(SecondNodeKeys);
                    String capital = (String) SecondNodeValue.get(REGION_CAPITAL);
                    String photo = (String) SecondNodeValue.get(REGION_PHOTO);

                    countryList.add(new Country(firstNodeKeys, SecondNodeKeys, capital, photo));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Country> setRandAnswers() {
        List<Country> tempList = new ArrayList<>();
        List<Country> tempAnswers = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        tempList.addAll(countryList);

        if (tempList.size() > 4) {
            for (int i = 0; i < 4; i++) {
                index.add(new Random().nextInt(tempList.size()));
                tempAnswers.add(tempList.get(index.get(i)));
                tempList.remove(tempList.get(index.get(i)));
            }
            tempList.add(tempAnswers.get(0));
            tempList.add(tempAnswers.get(1));
            tempList.add(tempAnswers.get(2));

            Log.d(TAG, "setRandAnswers: DEl   " + tempAnswers.get((3)).getCountry());
            removeCountry(tempAnswers.get((3)));

            Log.d(TAG, "tempList  " + tempList.size() + " / " + "countryList  " + countryList.size());
            Log.d(TAG, "LIst Index " + tempAnswers.get(0).getCountry() + " / " + tempAnswers.get(1).getCountry()
                    + " / " + tempAnswers.get(2).getCountry() + " / " + tempAnswers.get(3).getCountry());
            Log.d(TAG, "--------------------------------------------------------------------");
        } else if (tempList.size() > 3) {
            Log.d(TAG, "--------------------------- >3 -----------------------------------------" + tempList.get(2).getCountry());
            for (int i = 0; i < 3; i++) {
                index.add(new Random().nextInt(tempLite.size()));
                tempAnswers.add(tempLite.get(index.get(i)));
                tempLite.remove(tempLite.get(index.get(i)));
            }
            tempLite.add(tempAnswers.get(0));
            tempLite.add(tempAnswers.get(1));
            tempLite.add(tempAnswers.get(2));

            tempAnswers.add(tempList.get(2));
            removeCountry(tempList.get(2));

            Log.d(TAG, "setRandAnswers: DEl   " + tempAnswers.get((3)).getCountry());
            Log.d(TAG, "tempLite  " + tempLite.size() + " / " + "countryList  " + countryList.size() + " / " + countryList.get(0).getCountry() + " / "
                    + countryList.get(1).getCountry() + " / " + countryList.get(2).getCountry());
            Log.d(TAG, "LIst Index " + tempAnswers.get(0).getCountry() + " / " + tempAnswers.get(1).getCountry()
                    + " / " + tempAnswers.get(2).getCountry() + " / " + tempAnswers.get(3).getCountry());
            Log.d(TAG, "--------------------------------------------------------------------");
        } else if (tempList.size() > 2) {
            Log.d(TAG, "--------------------------- >2 -----------------------------------------");
            for (int i = 0; i < 3; i++) {
                index.add(new Random().nextInt(tempLite.size()));
                tempAnswers.add(tempLite.get(index.get(i)));
                tempLite.remove(tempLite.get(index.get(i)));
            }
            tempLite.add(tempAnswers.get(0));
            tempLite.add(tempAnswers.get(1));
            tempLite.add(tempAnswers.get(2));

            tempAnswers.add(tempList.get(1));
            removeCountry(tempList.get(1));

            Log.d(TAG, "setRandAnswers: DEl   " + tempList.get((1)).getCountry());
            Log.d(TAG, "countryList  " + countryList.size() + " / " + countryList.get(0).getCountry() + " / "
                    + countryList.get(1).getCountry());
            Log.d(TAG, "LIst Index " + tempAnswers.get(0).getCountry() + " / " + tempAnswers.get(1).getCountry()
                    + " / " + tempAnswers.get(2).getCountry() + " / " + tempAnswers.get(3).getCountry());
            Log.d(TAG, "--------------------------------------------------------------------");
        } else if (tempList.size() > 1) {
            Log.d(TAG, "--------------------------- >1 -----------------------------------------");
            for (int i = 0; i < 3; i++) {
                index.add(new Random().nextInt(tempLite.size()));
                tempAnswers.add(tempLite.get(index.get(i)));
                tempLite.remove(tempLite.get(index.get(i)));
            }
            tempLite.add(tempAnswers.get(0));
            tempLite.add(tempAnswers.get(1));
            tempLite.add(tempAnswers.get(2));

            tempAnswers.add(tempList.get(0));
            removeCountry(tempList.get(0));

            Log.d(TAG, "setRandAnswers: DEl   " + tempList.get((0)).getCountry());
            Log.d(TAG, "countryList  " + countryList.size() + " / " + countryList.get(0).getCountry());
            Log.d(TAG, "LIst Index " + tempAnswers.get(0).getCountry() + " / " + tempAnswers.get(1).getCountry()
                    + " / " + tempAnswers.get(2).getCountry() + " / " + tempAnswers.get(3).getCountry());
            Log.d(TAG, "--------------------------------------------------------------------");
        } else {
            answer1.setEnabled(false);
            Log.d(TAG, "--------------------------- Finis -----------------------------------------");
        }
        return tempAnswers;
    }

    private void removeCountry(Country country) {
        Log.d(TAG, "removeCountry: --------------" + country.getCountry() + "------------  " + tempLite.size());
        if (countryList.size() > 1) {
            countryList.remove(country);
            tempLite.add(country);
        } else {
            Log.d(TAG, "removeCountry: no deleted");
        }
    }

}
