package flag.test.com.contryflag;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
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
import java.util.Random;

import flag.test.com.contryflag.model.Country;

public class MainActivityFragment extends Fragment {

    private static final String JSON_ANSWER = "answer.json";
    private static final String REGION_CAPITAL = "capital";
    private static final String REGION_PHOTO = "photo";
    private static final String ANSWER_1 = "answer1";
    private static final String BUNDLE_LIST_ANSWERS = "model.bundle.ListAnswer";

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
    private List<Country> tempList = new ArrayList<>();
    private List<Country> listAnswer = new ArrayList<>();
    private String region;

    private List<Country> bundleListAnswer = new ArrayList<>();
    private static final String TAG = "MainActivityFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAnswersJSON();
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

        init(false);

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(false);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_LIST_ANSWERS, (ArrayList<? extends Parcelable>) listAnswer);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            bundleListAnswer = savedInstanceState.getParcelableArrayList(BUNDLE_LIST_ANSWERS);
            init(true);
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

    private void getAnswersJSON() {
        countryList = new ArrayList<>();
        try {
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

    private void getRandAnswers(boolean save) {
        if (save){
            listAnswer = bundleListAnswer;
        }else {
            listAnswer = setRandAnswers();
        }

        if (listAnswer.size() > 1) {
            answer1.setText(listAnswer.get(0).getCountry());
            answer2.setText(listAnswer.get(1).getCountry());
            answer3.setText(listAnswer.get(2).getCountry());
            answer4.setText(listAnswer.get(3).getCountry());
            //Log.d(TAG, "getRandAnswers: " + listAnswer.get(3).getCountry());
            try {
                region = listAnswer.get(3).getRegion();
                stream = getContext().getAssets()
                        .open(region + "/" + listAnswer.get(3).getPhoto());
                bitmapFlag = BitmapFactory.decodeStream(stream);
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
            Log.d(TAG, "getRandAnswers:  Finish---------------------------------------");
        }
    }

    private List<Country> setRandAnswers() {
        List<Country> tempAnswers = new ArrayList<>();
        if (tempList.size() > 1 ){
            tempList.clear();
        }
        tempList.addAll(countryList);

        if (tempList.size() > 4) {
            tempAnswers = setRandAnswers(tempList, 4);
        } else if (tempList.size() > 3) {
            tempAnswers = setRandAnswers(tempLite, 3);
        } else if (tempList.size() > 2) {
            tempAnswers = setRandAnswers(tempLite, 2);
        } else if (tempList.size() > 1) {
            tempAnswers = setRandAnswers(tempLite, 1);
        } else {
            answer1.setEnabled(false);
            Log.d(TAG, "--------------------------- Finis -----------------------------------------");
        }
        return tempAnswers;
    }

    private List<Country> setRandAnswers(List<Country> list, int length) {
        Log.d(TAG, "setRandAnswers: ***************************************   " + tempList.size());
        List<Integer> index = new ArrayList<>();
        List<Country> answers = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            index.add(new Random().nextInt(list.size()));
            answers.add(list.get(index.get(i)));
            list.remove(list.get(index.get(i)));
        }
        for (int i = 0; i < 3; i++) {
            list.add(answers.get(i));
        }

        switch (length) {
            case 4:
                removeCountry(answers.get((3)));
                break;
            case 3:
                answers = setAnswerHelper(answers, 2);
                Log.d(TAG, "setRandAnswers: lite size  " + tempLite.size());
                Log.d(TAG, "setRandAnswers:  3  " + tempList.get(length - 1).getCountry());
                break;
            case 2:
                answers = setAnswerHelper(answers, 1);
                Log.d(TAG, "setRandAnswers: lite size  " + tempLite.size());
                Log.d(TAG, "setRandAnswers:  2  " + tempList.get(length - 1).getCountry());
                break;
            case 1:
                answers = setAnswerHelper(answers, 0);
                Log.d(TAG, "setRandAnswers: lite size  " + tempLite.size());
                Log.d(TAG, "setRandAnswers:  1  " + tempList.get(length - 1).getCountry());
                break;
        }
        return answers;
    }
    private List<Country> setAnswerHelper(List<Country> answers, int index){
        answers.add(tempList.get(index));
        removeCountry(tempList.get(index));
        return  answers;
    }

    private void removeCountry(Country country) {
        if (countryList.size() > 1) {
            countryList.remove(country);
            tempLite.add(country);
        } else {
            Log.d(TAG, "removeCountry: no deleted");
        }
    }

    private void init(boolean save){
        getRandAnswers(save);
        txtRegion.setText(region);
        countryFlagImg.setImageBitmap(bitmapFlag);
    }
}
