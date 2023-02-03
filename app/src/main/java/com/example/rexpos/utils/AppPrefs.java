package com.example.rexpos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {

    private SharedPreferences mPrefs;



    public final int  LOGIN_USER_ID_UNDEFINED = 0;
    private static String LOGIN_USER_ID_DEFINED = "user_id";

    public final String  LOGIN_USER_EMAIL_UNDEFINED = "";
    private static String LOGIN_USER_EMAIL_DEFINED = "user_email";


    public final String  LOGIN_USER_SHOP_ID_UNDEFINED = "";
    private static String LOGIN_USER_SHOP_ID_DEFINED = "user_shop_id";


    public final String  LOGIN_USER_SHOP_NAME_UNDEFINED = "";
    private static String LOGIN_USER_SHOP_NAME_DEFINED = "user_shop_name";



    public final String  LOGIN_USER_PHONE_UNDEFINED = "";
    private static String LOGIN_USER_PHONE_DEFINED = "user_phone";



    public final String  LOGIN_USER_ADDRESS_UNDEFINED = "";
    private static String LOGIN_USER_ADDRESS_DEFINED = "user_address";




    public final String  LOGIN_USER_PASSWORD_UNDEFINED = "";
    private static String LOGIN_USER_PASSWORD_DEFINED = "user_password";


    //public final String  SERVER_URL_UNDEFINED = "http://phrt.com.au";
    public final String  SERVER_URL_UNDEFINED = "http://erpmessenger.com";
    private static String SERVER_URL_DEFINED = "server_url";


    public final String  USER_NAME_UNDEFINED = "";
    private static String USER_NAME_DEFINED = "username";

    public final String  FCM_TOKEN_UNDEFINED = "";
    private static String FCM_TOKEN_DEFINED = "fcm_token";




    public static AppPrefs create(Context context){
        return new AppPrefs(context);
    }


    public AppPrefs(Context context){
        mPrefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

    public void putInt(String key, int value){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void removePref(String key){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(key);
        editor.apply();
    }



    public int getUserID(){return mPrefs.getInt(LOGIN_USER_ID_DEFINED, LOGIN_USER_ID_UNDEFINED);}
    public void setUserID(int  newValue){putInt(LOGIN_USER_ID_DEFINED, newValue);}

    public String getUserEmail(){return mPrefs.getString(LOGIN_USER_EMAIL_DEFINED, LOGIN_USER_EMAIL_UNDEFINED);}
    public void setUserEmail(String  newValue){putString(LOGIN_USER_EMAIL_DEFINED, newValue);}


    public String getUserShopId(){return mPrefs.getString(LOGIN_USER_SHOP_ID_DEFINED, LOGIN_USER_SHOP_ID_UNDEFINED);}
    public void setUserShopId(String  newValue){putString(LOGIN_USER_SHOP_ID_DEFINED, newValue);}


    public String getUserShopName(){return mPrefs.getString(LOGIN_USER_SHOP_NAME_DEFINED, LOGIN_USER_SHOP_NAME_UNDEFINED);}
    public void setUserShopName(String  newValue){putString(LOGIN_USER_SHOP_NAME_DEFINED, newValue);}


    public String getUserPhone(){return mPrefs.getString(LOGIN_USER_PHONE_DEFINED, LOGIN_USER_PHONE_UNDEFINED);}
    public void setUserPhone(String  newValue){putString(LOGIN_USER_PHONE_DEFINED, newValue);}


    public String getUserAddress(){return mPrefs.getString(LOGIN_USER_ADDRESS_DEFINED, LOGIN_USER_ADDRESS_UNDEFINED);}
    public void setUserAddress(String  newValue){putString(LOGIN_USER_ADDRESS_DEFINED, newValue);}



    public String getUserPassword(){return mPrefs.getString(LOGIN_USER_PASSWORD_DEFINED, LOGIN_USER_PASSWORD_UNDEFINED);}
    public void setUserPassword(String  newValue){putString(LOGIN_USER_PASSWORD_DEFINED, newValue);}

    public String getUsername(){return mPrefs.getString(USER_NAME_DEFINED, USER_NAME_UNDEFINED);}
    public void setUsername(String  newValue){putString(USER_NAME_DEFINED, newValue);}



    public void setServerUrl(String serverUrl){
        putString(SERVER_URL_DEFINED, serverUrl);
    }
    public String getServerUrl(){
        return mPrefs.getString(SERVER_URL_DEFINED, SERVER_URL_UNDEFINED);
    }
  public void setFcmToken(String fcmToken){
        putString(FCM_TOKEN_DEFINED, fcmToken);
    }
    public String getFcmToken(){
        return mPrefs.getString(FCM_TOKEN_DEFINED, FCM_TOKEN_UNDEFINED);
    }




}
