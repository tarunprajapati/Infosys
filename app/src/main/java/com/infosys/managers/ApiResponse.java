package com.infosys.managers;

import org.json.JSONObject;

import java.util.HashMap;


public class ApiResponse {
    private int errorCode = -1;
    private String errorMsg = null;
    //private HashMap<String, JSONObject> resMapInt = null;
    private HashMap<Integer, JSONObject> resMapInt = null;
    private HashMap<Integer, String> resStringInt = null;
    private HashMap<String, JSONObject> resMap = null;
    private HashMap<String, String> resString = null;
    private String successMsg = null;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HashMap<Integer, JSONObject> getResMapInt() {
        return resMapInt;
    }

    public HashMap<Integer, String> getResStringInt() {
        return resStringInt;
    }


    public HashMap<String, JSONObject> getResMap() {
        return resMap;
    }

    public HashMap<String, String> getResString() {
        return resString;
    }

    public void addResponce(Integer reqCode, JSONObject responce) {
        if (resMapInt == null)
            resMapInt = new HashMap<Integer, JSONObject>();

        resMapInt.put(reqCode, responce);
    }

    public void addResponce(Integer url, String responce) {
        if (resStringInt == null)
            resStringInt = new HashMap<Integer, String>();

        resStringInt.put(url, responce);
    }

    public void addResponce(String reqCode, JSONObject responce) {
        if (resMap == null)
            resMap = new HashMap<String, JSONObject>();

        resMap.put(reqCode, responce);
    }

    public void addResponce(String url, String responce) {
        if (resString == null)
            resString = new HashMap<String, String>();

        resString.put(url, responce);
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }
}
