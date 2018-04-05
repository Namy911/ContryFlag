package flag.test.com.contryflag;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import flag.test.com.contryflag.model.Country;

public class BeginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BeginFragment";
    private static final String JSON_ANSWER = "answer2.json";
    private static final String REGION_CAPITAL = "capital";
    private static final String REGION_PHOTO = "photo";
    public static final String BUNDLE_FLAG_STATE = "flag.state";

    private InputStream stream;
    private Button btnStart;
    private boolean flagState;

    public static BeginFragment newInstance() {
        Bundle args = new Bundle();

        BeginFragment fragment = new BeginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Country.getInstance().getArray().size() < 1) {
            getAnswersJSON();
            Log.d(TAG, "onClick: " + Country.getInstance().getArray().size());
        } else {
            Log.d(TAG, "onCreate: else flag" + Country.getInstance().getArray().size());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_begin, container, false);

        btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_FLAG_STATE, flagState);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            btnStart.setVisibility(View.GONE);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, MainActivityFragment.newInstance())
                    .commit();
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

                    Country.getInstance().addToArray(firstNodeKeys, SecondNodeKeys, capital, photo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
