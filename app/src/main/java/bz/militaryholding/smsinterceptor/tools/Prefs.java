package bz.militaryholding.smsinterceptor.tools;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Prefs {
    private final String PREFS = "Prefs";
    private final String KEY_SCRIPT_URL = "scriptUrl";
    private final String KEY_BANKS = "banks";
    private final String KEY_JO_BANK = "bank";

    private static Prefs prefs;

    private Context context;

    private Prefs(Context context) {
        this.context = context;
    }

    public static Prefs getInstance(Context context) {
        if(prefs == null) {
            prefs = new Prefs(context);
        }

        return prefs;
    }

    private SharedPreferences getSP() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public String getScriptUrl() {
        return getSP().getString(KEY_SCRIPT_URL, "");
    }

    public void saveScriptUrl(String scriptUrl) {
        getSP().edit().putString(KEY_SCRIPT_URL, scriptUrl).commit();
    }

    public void saveBanks(List<String> banks) {
        JSONArray ja = new JSONArray();;
        try {
            for(String bank : banks) {
                JSONObject jo = new JSONObject();
                jo.put(KEY_JO_BANK, bank.toUpperCase());
                ja.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getSP().edit().putString(KEY_BANKS, ja.toString()).commit();
    }

    public List<String> getBanks() {
        List<String> banks = new ArrayList<>();
        try {
            String s = getSP().getString(KEY_BANKS, "");
            if(!s.isEmpty()) {
                JSONArray ja = new JSONArray(s);
                for(int  i=0; i<ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    banks.add(jo.getString(KEY_JO_BANK));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return banks;
    }
}