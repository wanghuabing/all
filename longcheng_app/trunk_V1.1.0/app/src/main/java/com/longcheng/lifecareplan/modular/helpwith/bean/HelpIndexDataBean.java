package com.longcheng.lifecareplan.modular.helpwith.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.lifecareplan.bean.ResponseBean;

/**
 * 作者：jun on
 * 时间：2018/8/21 13:12
 * 意图：返回对象
 */

public class HelpIndexDataBean extends ResponseBean {
    @SerializedName("data")
    private HelpIndexAfterBean data;

    public HelpIndexAfterBean getData() {
        return data;
    }

    public void setData(HelpIndexAfterBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "data =" + data + " , msg = " + msg + " , status = " + status;
    }
}
