package com.tigan_lab.easy_clean.Constants;

import org.json.JSONArray;

public class Config {
    public static final String MyPrefreance = "my_preprence";
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String USER_STATUS = "user_status";
    public static final String DEVICE_ID = "device_id";
    public static final String PASSWORD = "password";

    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PINCODE = "pinCode";
    public static final String COUNTRY = "country";
    public static final String LAT = "lat";
    public static final String LONG = "long";


    static final String APP_NAME = "QucikService";
    public static final String PREFS_NAME = "QuickServiceLoginPrefs";
    public static final String PREFS_NAME2 = "QuickServiceLoginPrefs2";
    public static final String IS_CUST_LOGIN = "false";
    public static final String IS_SP_LOGIN = "false";
    public static final String LOGIN_TYPE = "none";

    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_ID = "user_id";
    public static final String KEY_MOBILE = "user_phone";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_SOCITY_ID = "Socity_id";
    public static final String KEY_SOCITY_NAME = "socity_name";
    public static final String KEY_HOUSE = "house_no";
    public static final String KEY_PASSWORD = "password";

    public static String Coupocode;
    public static String Totalamount;

    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String WALLET_TOTAL_AMOUNT = "WALLET_TOTAL_AMOUNT";
    public static final String COUPON_TOTAL_AMOUNT = "COUPON_TOTAL_AMOUNT";
    public static final String KEY_WALLET_Ammount = "wallet_ammount";
    public static final String KEY_REWARDS_POINTS = "rewards_points";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_REWARDS = "rewards";

    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";

    public static String ADD_ON_Amount;
    public static JSONArray cat_id_json_array;
    //Store Selection

    public static final String KEY_STORE_COUNT = "STORE_COUNT";
    public static final String KEY_NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

    //Firebase
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final String CITY_ID = "CITY_ID";
    public static final String STORE_ID = "STORE_ID";
    public static final String DELIVERY_RANGE = "DELIVERY_RANGE";
    public static final String PROFESSION = "PROFESSION";

    public static String BASE_URL = "https://www.easycleasapp.tiganinc.com/api/";
    public static String IMAGE_URL = "https://www.easycleasapp.tiganinc.com/"; //
    public static String Login = BASE_URL + "partnerlogin";
    public static String SignUP = BASE_URL + "partner_register";

    public static String CityListUrl = BASE_URL + "citylist";
    public static String MAINCATELIST = BASE_URL + "category";
    public static String CategyList = BASE_URL + "child_category_list";
    public static String ChildCategyList = BASE_URL + "sub_child_cat_list";
    public static String Profile = BASE_URL + "partnerProfile";
    public static String UPDATEPass = BASE_URL + "profileupdatepassword";

    public static String leadlist = BASE_URL + "booking_list_for_partner";
    public static String serviceDetails = BASE_URL + "onbookingclick";
    public static String BuyBooking = BASE_URL + "partnerbuybooking";
    public static String StartBooking = BASE_URL + "partnerstartbooking";
    public static String markedAsComplete = BASE_URL + "partnermarkedcompleted";

    public static String onGoing = BASE_URL + "partnerongoing";
    public static String histoRY = BASE_URL + "partnerbookinghistory";
    public static String WITHDRAWAL = BASE_URL + "complete_ord";

    public static String UPI = BASE_URL + "update_upi";
    public static String SEND_REQ = BASE_URL + "send_withdrawreq";

    public static String bankAadd = BASE_URL + "bank";
    public static String bankUpdate = BASE_URL + "bankupdate";
    public static String bankShow = BASE_URL + "banklisting";

    public static String gstAadd = BASE_URL + "documentation";
    public static String gstUpdate = BASE_URL + "documentationupdate";
    public static String gstShow = BASE_URL + "documentationlist";

    public static String allearnigs = BASE_URL + "leadboughthistory";
    public static String rechargehistory = BASE_URL + "rechargehistory";
    public static String rechargePLan = BASE_URL + "rechargeplan";


    public static String termsCondtiosn = BASE_URL + "terms_condition";
    public static String contctUS = BASE_URL + "about_us";
    public static String ADD_ORDER_URL = BASE_URL + "rechargevalueinsert";
    public static String COINS = BASE_URL + "partnercoins";

    public static String PROFILEUPDATE = BASE_URL + "partner_profile_update";
    public static String forgotPss = BASE_URL + "partner_pass_otp";
    public static String veriFYOtp = BASE_URL + "verify_otp_pass";
    public static String UpdatePaass = BASE_URL + "update_partner_pass";
}
