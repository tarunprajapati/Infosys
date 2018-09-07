package com.infosys.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.infosys.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Utils {
    public static HashMap<String, String> jsonToMap(JSONObject t) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = t;
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

//        System.out.println("json : " + jObject);
//        System.out.println("map : " + map);
        return map;
    }


    public static ArrayList<HashMap<String, String>> getArrayListFromJsonArray(JSONArray subcat_array) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp_map = null;
        for (int i = 0; i < subcat_array.length(); i++) {
            try {
                temp_map = jsonToMap(subcat_array.getJSONObject(i));
                list.add(temp_map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        try {
            progressDialog.show();
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return progressDialog;

    }


    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public static boolean isPasswordStrong(String inputstring) {
        boolean flag = false;

        Pattern p = Pattern.compile("(?=^.{8,40}$)(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%!])(?!.*\\s).*$");
        Matcher m = p.matcher(inputstring);
        return m.find();

    }


    public static void setMinimumPasswordValidation(EditText... editTexts) {

    }

    /**
     * To get a value from string of hashmap by passing key that existed in Hashmap before converting to String.
     * Sample string: {fld_category=Principal category, test=test 1, fld_categoryID=1}
     *
     * @param string
     * @param key
     * @return value
     */
    public static String getValueFromStringOfHashMap(String string, String key) {
        int start_index = string.indexOf(key) + key.length() + 1;
        int end_index = string.indexOf(",", start_index);
        if (end_index == -1) { // because last key value pair doesn't have trailing comma (,)
            end_index = string.indexOf("}");
        }
        String value = string.substring(start_index, end_index);

        return value;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    public static String getUserAgent() {
        return "Device " + Build.MANUFACTURER + " " + Build.MODEL + ", Android Version " + Build.VERSION.RELEASE;
    }


    public static String getHumanReadableDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

    public static String getHumanReadableDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

    public static JSONObject getJsonFromResponse(Response<ResponseBody> response) {

        JSONObject responseJsonObject = null;
        try {
            if (response.body() != null) {
                responseJsonObject = new JSONObject(response.body().string());
            } else if (response.errorBody() != null) {
                responseJsonObject = new JSONObject(response.errorBody().string());
            }
            if (BuildConfig.DEBUG) {
                Log.d("Service response: ", responseJsonObject.toString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                responseJsonObject = new JSONObject(response.errorBody().string());
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return responseJsonObject;
    }

    public static HashMap<String, String> modelToMap(Object model) throws JSONException {
        return Utils.jsonToMap(new JSONObject(new Gson().toJson(model)));
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }
}
