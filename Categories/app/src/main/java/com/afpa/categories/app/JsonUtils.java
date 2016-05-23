package com.afpa.categories.app;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by syjebrane on 20/05/2016.
 */
public final class JsonUtils {

    public static final String ENCODING_UTF8 = "UTF-8";

    public static Map<String, String> parse(JSONObject json, Map<String, String> out) throws JSONException {
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String val = null;
            try {
                JSONObject value = json.getJSONObject(key);
                parse(value, out);
            } catch (Exception e) {
                val = json.getString(key);
            }

            if (val != null) {
                out.put(key, val);
            }
        }
        return out;
    }

    public static List<String> getKeysAsList(JSONObject json, List<String> list) {
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            list.add(key);
        }
        return list;
    }

    public static JSONObject loadJSONFromResources(Context context, int resourceId) throws IOException {
        BufferedReader bufferedReader = null;
        JSONObject json = null;
        try {
            InputStream inStream = context.getResources().openRawResource(R.raw.champs);

            BufferedInputStream bufferedStream = new BufferedInputStream(inStream);
            InputStreamReader reader = new InputStreamReader(bufferedStream, "ISO-8859-1");
            bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
            return new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return null;
    }
}
