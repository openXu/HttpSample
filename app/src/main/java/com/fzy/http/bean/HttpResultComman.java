package com.fzy.http.bean;

import java.util.ArrayList;

/**
 * Created by Admin on 2017/7/14.
 */

public class HttpResultComman<T> {

    private String ret;
    private String msg;
    private ArrayList<T> data;

    @Override
    public String toString() {
        return "HttpResultComman{" +
                "ret='" + ret + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
