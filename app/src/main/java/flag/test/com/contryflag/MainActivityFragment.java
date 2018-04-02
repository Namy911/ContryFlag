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

public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private static final String JSON_ANSWER = "answer2.json";
    private static final String REGION_CAPITAL = "capital";
    private static final String REGION_PHOTO = "photo";
    private static final String BUNDLE_LIST_ANSWERS = "model.bundle.ListAnswer";
    private static final String BUNDLE_ANSWERS = "fragment.main.answer";

    private ImageView countryFlagImg;
    private InputStream stream;
    private TextView txtRegion;
    private List<Button> buttonList = new ArrayList<Button>();
    private Bitmap bitmapFlag;

    private List<Country> countryList;
    private List<Country> tempLite = new ArrayList<>();
    private List<Country> tempList = new ArrayList<>();
    private List<Country> listAnswer = new ArrayList<>();
    private String region;
    private Country answer;

    private Country bundleAnswer;
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

        Button button1 = view.findViewById(R.id.answer1);
        button1.setOnClickListener(this);
        Button button2 = view.findViewById(R.id.answer2);
        button2.setOnClickListener(this);
        Button button3 = view.findViewById(R.id.answer3);
        button3.setOnClickListener(this);
        Button button4 = view.findViewById(R.id.answer4);
        button4.setOnClickListener(this);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);

        init(false);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_LIST_ANSWERS, (ArrayList<? extends Parcelable>) listAnswer);
        outState.putParcelable(BUNDLE_ANSWERS,  answer);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            bundleListAnswer = savedInstanceState.getParcelableArrayList(BUNDLE_LIST_ANSWERS);
            bundleAnswer = savedInstanceState.getParcelable(BUNDLE_ANSWERS);
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
            answer = bundleAnswer;
        }else {
            listAnswer = setRandAnswers();
            //answer = selectAnswer();
        }
        //Log.d(TAG, "getRandAnswers:  " + answer.getCountry());
        if (listAnswer.size() > 1) {
            for (int i = 0; i < buttonList.size(); i++) {
                buttonList.get(i).setText(listAnswer.get(i).getCountry());
            }
            try {
                region = answer.getRegion();
                stream = getContext().getAssets()
                        .open(region + "/" + answer.getPhoto());
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
            buttonList.get(0).setEnabled(false);
            buttonList.get(1).setEnabled(false);
            buttonList.get(2).setEnabled(false);
            buttonList.get(3).setEnabled(false);
            Log.d(TAG, "--------------------------- Finis -----------------------------------------");
        }
        return tempAnswers;
    }

    private List<Country> setRandAnswers(List<Country> list, int length) {
        List<Integer> index = new ArrayList<>();
        List<Country> answers = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            index.add(new Random().nextInt(list.size()));
            answers.add(list.get(index.get(i)));
            list.remove(list.get(index.get(i)));
        }

        switch (length) {
            case 4:
                setRandAnswer(answers, list);
                break;
            case 3:
                setAnswerHelper(answers, list, 2);
                break;
            case 2:
                setAnswerHelper(answers, list,1);
                break;
            case 1:
                setAnswerHelper(answers, list, 0);
                break;
        }
        return answers;
    }

    private void setRandAnswer(List<Country> answers, List<Country> list){
        int id = new Random().nextInt(answers.size());
        for (int i = 0; i < 4; i++) {
            if (id == i){
                continue;
            }else {
                list.add(answers.get(i));
            }
        }
        answer = answers.get(id);
        Log.d(TAG, "setRandAnswer: > 4 " + answers.get(id).getCountry());
        removeCountry(answer);
    }

    private void setAnswerHelper(List<Country> answers,List<Country> list, int index){
        int id = new Random().nextInt(answers.size());
        for (int i = 0; i < 4; i++) {
            list.add(answers.get(i));
        }
        answers.remove(id);
        answers.add( id, tempList.get(index));

        removeCountry(tempList.get(index));
        answer = tempList.get(index);
        Log.d(TAG, "setAnswerHelper: answer   " + answer.getCountry() + answers.size());
    }

    private void removeCountry(Country country) {
        if (countryList.size() > 1) {
            countryList.remove(country);
            tempLite.add(country);
            //Log.d(TAG, "setAnswerHelper Delete  : " + countryList.size() );
        } else {
            Log.d(TAG, "removeCountry: no deleted");
        }
    }

    private void init(boolean save){
        getRandAnswers(save);
        txtRegion.setText(region);
        countryFlagImg.setImageBitmap(bitmapFlag);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.answer1 || id == R.id.answer2 || id == R.id.answer3 || id == R.id.answer4){
            Button button = (Button) v;
            String text = button.getText().toString();
            if (text.equals(answer.getCountry())){
                init(false);
               // Log.d(TAG, "onClick: "+" Answerrrrrrrrrrrrrrrrrr");
            }else {
                Log.d(TAG, "onClick: " + "Incorect!!!!!!!!!!!");
            }


            //init(false);
        }
    }
}
