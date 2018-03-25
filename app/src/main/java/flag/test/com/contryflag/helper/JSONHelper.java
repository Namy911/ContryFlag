package flag.test.com.contryflag.helper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
    private void jsonSetter(JSONArray array, String key, Object replace) {
        for (int i=0; i<array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                if (obj.has(key)) {
                    obj.putOpt(key,replace);
                }
            } catch (JSONException e) {
// log("jsonSetter exception");
            }
        }
    }

    private Object jsonGetter(JSONArray json, String key) {
        Object value = null;
        for (int i=0; i<json.length(); i++) {
            try {
                JSONObject obj = json.getJSONObject(i);
                if (obj.has(key)) {
                    value = obj.get(key);
                }
            } catch (JSONException e) {
// log("jsonGetter Exception=" +e);
            }
        }
        return value;
    }
}
