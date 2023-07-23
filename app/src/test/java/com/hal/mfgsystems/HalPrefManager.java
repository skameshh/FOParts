package com.hal.mfgsystems;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class HalPrefManager {

    private static final String PREF_NAME = "com.HAL.ASSET.PREF_NAME";

    public static void putInPref(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editore = settings.edit();
        //editore.putString("USER", "Selvi");
        editore.putString(key, value);
        editore.apply();
    }

    public static String readFromPref(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String value = settings.getString(key, "");
        return value;
    }


    public static void saveObjectToSharedPreference(Context context,  String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }
}
