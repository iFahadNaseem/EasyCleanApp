package com.tigan_lab.customer.Extra;

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


    static final String APP_NAME = "QuickService";
    public static final String PREFS_NAME = "QuickServiceLoginPrefs";
    public static final String PREFS_NAME2 = "QuickServiceLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";

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
    public static String Finalam;
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


    //City and Store Id
    public static final String CITY_ID = "CITY_ID";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String STORE_ID = "STORE_ID";




    public static String BASE_URL = "https://www.easycleasapp.tiganinc.com/api/";
    public static String IMAGE_URL = "https://www.easycleasapp.tiganinc.com/";



    public static String Login = BASE_URL + "login";
    public static String SignUP = BASE_URL + "signUp";
    public static String SignUPOTP = BASE_URL + "verifyOtp";
    public static String OTP = BASE_URL + "verifyOtpforpass";
    public static String FORGOTOTP = BASE_URL + "otpforpass";
    public static String UPDATEPass = BASE_URL + "updateusrpass";
    public static String Profile = BASE_URL + "userProfile";
    public static String ProfileEdit = BASE_URL + "profileupdate";

    public static String HomeBanner = BASE_URL + "banner";
    public static String HomeBanner1 = BASE_URL + "lowebanner";
    public static String HomeCategory = BASE_URL + "category";
    public static String HomePopularService = BASE_URL + "popular_city_services";

    public static String HomeSubCategory = BASE_URL + "sub_category";
    public static String HomeChildCategory = BASE_URL + "child_category";
    public static String HomeService = BASE_URL + "city_services";
    public static String ServiceDetails = BASE_URL + "blog";


    public static String ShowAddress = BASE_URL + "showaddress";
    public static String DeleteAddress = BASE_URL + "DeleteUserAddress";
    public static String EditAddress = BASE_URL + "editaddress";
    public static String AddAdress = BASE_URL + "address";

    public static String showRating = BASE_URL + "showrating";
    public static String addRating = BASE_URL + "rating";
    public static String totalRating = BASE_URL + "totalrating";
    public static String ratinglist = BASE_URL + "ratinglist";

    public static String bookingonGOING = BASE_URL + "ongoing";
    public static String bookingHISTORY = BASE_URL + "history";

    public static String bookingCancelReasonList = BASE_URL + "cancelreasonlist";
    public static String bookingcancellation= BASE_URL + "cancellation";

    public static String SplitTime = BASE_URL + "SplitTime";
    public static String bookingReschedule = BASE_URL + "reschedule";

    public static String NotificAtionList = BASE_URL + "notification";
    public static String notificationCount = BASE_URL + "notificationCount";
    public static String notificationSeen = BASE_URL + "notificationSeen";

    public static String FAQList = BASE_URL + "faqlist";
    public static String FAQquesList = BASE_URL + "faqdesc";
    public static String FAQansList = BASE_URL + "faq_ans";

    public static String ADD_ORDER_URL = BASE_URL + "bookingadd";
    public static String add_on=BASE_URL+"addons";
    public static String aboutUS = BASE_URL + "about_us";
    public static String termsCondtiosn = BASE_URL + "terms_condition";

    public static final String GET_CITY_BOUNDRIES = "http://maps.google.com/maps/api/geocode/json?address=";

    public static String CouponOffr = BASE_URL + "coupon_cart_list";
    public static String coupon_applied = BASE_URL + "coupon_applied";
    public static String CityListUrl = BASE_URL + "citylist";
}