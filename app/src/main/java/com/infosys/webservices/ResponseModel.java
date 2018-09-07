package com.infosys.webservices;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Tarun on 7/9/16.
 */
public class ResponseModel {
    private String title;
    private JSONArray rows;


    private int status;
    private String msg;
    //private JSONObject data;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getRows() {
        return rows;
    }

    public void setRows(JSONArray rows) {
        if(rows == null || rows.length() == 0) {
            status = 0;
            msg = "No Data Found";
        }else
            status = 1;

        this.rows = rows;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return this.msg;
    }

    public JSONArray getData() {
        return rows;
    }
}
