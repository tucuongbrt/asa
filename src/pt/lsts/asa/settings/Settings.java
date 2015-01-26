package pt.lsts.asa.settings;

import pt.lsts.asa.ASA;

import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.otto.Bus;

public class Settings {

    private final static String TAG = "Settings";
	private static Settings settings = null;

	private Settings() {

	}

	public static Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	public static SharedPreferences getSharedPreferences() {
		return ASA.getInstance().sharedPreferences;
	}

	public static boolean putString(String key, String value) {
        boolean result = ASA.getInstance().sharedPreferences.edit().putString(key, value).commit();
        ASA.getInstance().getBus().post(key);
        Log.v(TAG, "ASA.getInstance().getBus().post(key);");
		return result;
	}

	public static boolean putInt(String key, int value) {
        boolean result = ASA.getInstance().sharedPreferences.edit().putInt(key, value).commit();
        ASA.getInstance().getBus().post(key);
        Log.v(TAG, "ASA.getInstance().getBus().post(key);");
		return result;
	}

	public static boolean putBoolean(String key, boolean value) {
        boolean result = ASA.getInstance().sharedPreferences.edit().putBoolean(key, value).commit();
        ASA.getInstance().getBus().post(key);
        Log.v(TAG, "ASA.getInstance().getBus().post(key);");
        return result;
	}

	public static boolean clear() {
		return ASA.getInstance().sharedPreferences.edit().clear().commit();
	}

	public static Map<String, ?> getAll() {
		return ASA.getInstance().sharedPreferences.getAll();
	}



    public static String getType(String key, String defValue){
        if (ASA.getInstance().sharedPreferences.contains(key)) {// get only Category
            return ASA.getInstance().sharedPreferences.getString(key,"null").split(",")[0];
        }
        return defValue;
    }

    public static String getCategory(String key, String defValue) {
        if (ASA.getInstance().sharedPreferences.contains(key)) {// get only Category
            Log.i(TAG,key+" .getString.toString= "+ASA.getInstance().sharedPreferences.getString(key,"null").toString());
            return ASA.getInstance().sharedPreferences.getString(key,"null").split(",")[1];
        }
        return defValue;
    }

    public static String getKey(String key, String defValue) {
		if (ASA.getInstance().sharedPreferences.contains(key)) {// get key to setting
            return ASA.getInstance().sharedPreferences.getString(key,"null").split(",")[2];
		}
		return defValue;
	}

    public static String getDescription(String key, String defValue){
        if (ASA.getInstance().sharedPreferences.contains(key)) {// get only Category
            return ASA.getInstance().sharedPreferences.getString(key,"null").split(",")[3];
        }
        return defValue;
    }


    public static String getString(String key, String defValue) {
        if (ASA.getInstance().sharedPreferences.contains(key)) {// remove Category
            String valueString = ASA.getInstance().sharedPreferences.getString(key,String.valueOf(defValue));
            return valueString.split(",")[4];
        }
        return defValue;
    }

    public static int getInt(String key, int defValue) {
        if (ASA.getInstance().sharedPreferences.contains(key)) {// remove Category
            String valueString = ASA.getInstance().sharedPreferences.getString(key,String.valueOf(defValue));
            return Integer.parseInt(valueString.split(",")[4]);
        }
        return defValue;
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (ASA.getInstance().sharedPreferences.contains(key)) {// remove Category
            String valueString = ASA.getInstance().sharedPreferences.getString(key,String.valueOf(defValue));
            return Boolean.parseBoolean(valueString.split(",")[4]);
        }
        return defValue;
    }

}
