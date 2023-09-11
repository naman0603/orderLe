package com.example.orderleapp.util;

import android.os.Environment;

public class Config {

    public static String TAG = "JYOJewellery";
    // preference file name
    public static final String PREF_FILE = TAG + "_PREF";
    public static String DB_NAME = "JYOJewellery.db";
    // Create a directory in SD CARD
    public static String APP_HOME = Environment.getExternalStorageDirectory()
            .getPath() + "/" + TAG;
    // A directory to store logs
    public static String DIR_LOG = APP_HOME + "/log";
    public static String DIR_USERDATA = APP_HOME + "/userdata";
    public static String DIR_IMAGES = APP_HOME + "/images";
    public static String DIR_IMAGES_TEMP = APP_HOME + "/.temp";

    public static String DIR_EQUALIZER = APP_HOME + "/equalizer";
    public static String DIR_RINGTONE = APP_HOME + "/ringtone";
    public static String DIR_VIDEO = APP_HOME + "/video";
    public static String BROADCAST_UPDATE = "BROADCAST_UPDATE";

    public static String SD_PATH = "file://";

    //    Live server URL
  /*  public static String HOST = "http://www.jyojewellery.com";
    public static String IMAGE_URL = HOST + "/app/plugins";
    public static String IMAGE_PRODUCTION_URL = IMAGE_URL + "/images/product_images/";
    public static String IMAGE_CATEGORY_URL = IMAGE_URL + "/images/category_images/";
    public static String API = HOST + "/app/v4";*/

    //    Local server
/*    public static String HOST = "https://200oksolutionssandbox.co.uk";
    public static String API = HOST + "/jyo/app/v4";
    public static String IMAGE_URL = HOST + "/jyo-php/app/plugins";
    public static String IMAGE_PRODUCTION_URL = IMAGE_URL + "/images/product_images/";
    public static String IMAGE_CATEGORY_URL = IMAGE_URL + "/images/category_images/";*/

    //    Local server
        public static String HOST = "http://200oksolutionsdemo.com";
    public static String API = HOST + "/order-le/app/v4";
    public static String IMAGE_URL = HOST + "/jyojewellery/app/plugins";
    public static String IMAGE_PRODUCTION_URL = IMAGE_URL + "/images/product_images/";
    public static String IMAGE_CATEGORY_URL = IMAGE_URL + "/images/category_images/";

//    public static String IMAGE_URL = HOST + "/jyo-dev/plugins";
//    public static String API = HOST + "/jyo-dev/v3";
//    public static String API = HOST + "/jyo-dev/v4";


    public static String LOGIN_API = API + "/login";
    public static String LOGOUT_API = API + "/logout";
    public static String CMS_API = API + "/cms";
    public static String FORGOT_PASSWORD_API = API + "/forgotpassword";
    public static String PRODUCT = API + "/product";
    public static String GET_ALL_CATEGORY = API + "/getallcategory";
    public static String GOLD_TYPE = API + "/goldtype";

    //APIS
    public static String API_LOGIN_USER = Config.LOGIN_API + "/userLogin";
    public static String API_LOGOUT_USER = Config.LOGOUT_API + "/userLogout";
    public static String API_FORGOT_PASSWORD = Config.FORGOT_PASSWORD_API + "/UserForgotpassword";
    public static String API_CMS = Config.CMS_API + "/cmsDetail";
    public static String API_UPDATE_PASSWORD = Config.LOGIN_API + "/changePassword";
    public static String API_GET_MYORDER = Config.PRODUCT + "/getMyOrders";
    public static String API_GET_ALL_CATEGORY = Config.GET_ALL_CATEGORY + "/getAllCategory";
    public static String API_GET_PRODUCT = Config.PRODUCT + "/getProduct";
    public static String API_GET_SEARCH_PRODUCT = Config.PRODUCT + "/searchProduct";
    public static String API_GET_PRODUCT_DETAILS = Config.PRODUCT + "/getProductDetail";
    public static String API_GET_ORDER_DETAIL = Config.PRODUCT + "/getOrderDetail";
    public static String API_REQUEST_PRODUCT = Config.PRODUCT + "/requestProduct";
    public static String API_GET_GOLD_TYPE = Config.GOLD_TYPE + "/getGoldType";
    public static String API_GOLD_TYPE_FILTER = Config.GOLD_TYPE + "/goldtypeFilter";
    public static String API_GET_BANNERS = Config.CMS_API + "/getBanners";

    // API TAGS
    // For each API we must have a TAG
    public static String TAG_LOGIN_USER = "API_LOGIN_USER";
    public static String TAG_FORGOT_PASSWORD = "API_FORGOT_PASSWORD";
    public static String TAG_LOGOUT_USER = "API_LOGOUT_USER";
    public static String TAG_CMS = "API_CMS";
    public static String TAG_UPDATE_PASSWORD = "API_UPDATE_PASSWORD";
    public static String TAG_GET_MYORDER = "TAG_GET_MYORDER";
    public static String TAG_GET_ALL_CATEGORY = "TAG_GET_ALL_CATEGORY";
    public static String TAG_GET_PRODUCT = "TAG_GET_PRODUCT";
    public static String TAG_GET_SEARCH_PRODUCT = "TAG_GET_SEARCH_PRODUCT";
    public static String TAG_GET_PRODUCT_DETAILS = "TAG_GET_PRODUCT_DETAILS";
    public static String TAG_GET_ORDER_DETAIL = "TAG_GET_ORDER_DETAIL";
    public static String TAG_REQUEST_PRODUCT = "TAG_REQUEST_PRODUCT";
    public static String TAG_GET_GOLD_TYPE = "TAG_GET_GOLD_TYPE";
    public static String TAG_GOLD_TYPE_FILTER = "TAG_GOLD_TYPE_FILTER";
    public static String TAG_GET_BANNERS = "TAG_GET_BANNERS";

    /*
     * Error and Warnings
     */
    public static String ERROR_NETWORK = "ERROR_NETWORK";
    public static String ERROR_API = "ERROR_API";
    public static String ERROR_UNKNOWN = "ERROR_UNKNOWN";

    public static int API_SUCCESS = 0;
    public static int API_FAIL = 1;
    public static int API_LOGIN_NOT_VERIFYED = 2;
    public static int API_LOGOUT_FROM_ALL_DEVICE = 3;
    public static int API_TOKEN_EXPIRE = 4;

    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 3000000;

    //Preferences
    public static String PREF_DEVICE_TOKEN = "PREF_DEVICE_TOKEN";
    public static String PREF_IS_LOGIN = "PREF_IS_LOGIN";//0=Not login,1=logged in
    public static String PREF_USERID = "PREF_USERID";
    public static String PREF_NAME = "PREF_NAME";
    public static String PREF_USERNAME = "PREF_USERNAME";
    public static String PREF_EMAIL = "PREF_EMAIL";
    public static String PREF_LOGIN_ACCESS_TOKEN = "PREF_LOGIN_ACCESS_TOKEN";
    public static String PREF_DEVICE_UNIQUE_ID = "PREF_DEVICE_UNIQUE_ID";
    public static String PREF_IS_UPDATE_APP = "PREF_IS_UPDATE_APP";
    //    public static String PREF_UPDATE_VERSION = "PREF_UPDATE_VERSION";
    public static String PREF_UPDATE_VERSION_FLOAT = "PREF_UPDATE_VERSION_FLOAT";

    public static String PREF_COMPANY_ID = "PREF_COMPANY_ID";
    public static String PREF_CODE = "PREF_CODE";
    public static String PREF_PICTURE_URL = "PREF_PICTURE_URL";

//    Branding Preferences

    public static String PREF_BRANDING_ID = "PREF_BRANDING_ID";
    public static String PREF_BRANDING_COMPANY_ID = "PREF_BRANDING_COMPANY_ID";
    public static String PREF_BRANDING_CODE = "PREF_BRANDING_CODE";
    public static String PREF_BRANDING_NAME = "PREF_BRANDING_NAME";
    public static String PREF_LOGO = "PREF_LOGO";
    public static String PREF_BRANDING_LOGO = "PREF_BRANDING_LOGO";
    public static String PREF_BRANDING_CONTACT = "PREF_BRANDING_CONTACT";
    public static String PREF_BRANDING_ADDRESS = "PREF_BRANDING_ADDRESS";
    public static String PREF_BRANDING_GST = "PREF_BRANDING_GST";
    public static String PREF_BRANDING_CIN = "PREF_BRANDING_CIN";
    public static String PREF_BRANDING_PAN = "PREF_BRANDING_PAN";
    public static String PREF_BRANDING_FROM_EMAIL = "PREF_BRANDING_FROM_EMAIL";
    public static String PREF_BRANDING_LOGO_URL = "PREF_BRANDING_LOGO_URL";

    //    Product Setting Detail Preferences
    public static String PREF_PRODUCT_CATEGORY = "PREF_PRODUCT_CATEGORY";
    public static String PREF_PRODUCT_GOLD_TYPE = "PREF_PRODUCT_GOLD_TYPE";
    public static String PREF_PRODUCT_CARAT_ID = "PREF_PRODUCT_CARAT_ID";
    public static String PREF_PRODUCT_NAME = "PREF_PRODUCT_NAME";
    public static String PREF_PRODUCT_ITEM_CODE = "PREF_PRODUCT_ITEM_CODE";
    public static String PREF_PRODUCT_SORT_ORDER = "PREF_PRODUCT_SORT_ORDER";
    public static String PREF_PRODUCT_PICTURE = "PREF_PRODUCT_PICTURE";
    public static String PREF_PRODUCT_WEIGHT = "PREF_PRODUCT_WEIGHT";
    public static String PREF_PRODUCT_DESCRIPTION = "PREF_PRODUCT_DESCRIPTION";
    public static String PREF_PRODUCT_SHORT_DESCRIPTION = "PREF_PRODUCT_SHORT_DESCRIPTION";
    public static String PREF_PRODUCT_SIZE = "PREF_PRODUCT_SIZE";
    public static String PREF_PRODUCT_WASTAGE = "PREF_PRODUCT_WASTAGE";
    public static String PREF_PRODUCT_STONE = "PREF_PRODUCT_STONE";

    public static String PREF_IS_LAYOUT = "PREF_IS_LAYOUT";




    /*
     * Cookie and SESSION
     */
    public static String PREF_SESSION_COOKIE = "sessionid";
    public static String SET_COOKIE_KEY = "Set-Cookie";
    public static String COOKIE_KEY = "Cookie";
    public static String SESSION_COOKIE = "sessionid";
}
