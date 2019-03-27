package com.longcheng.volunteer.modular.mine.myorder.tanksgiving.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.volunteer.bean.ResponseBean;

/**
 * 作者：jun on
 * 时间：2018/5/17 11:05
 * 意图：
 */

public class ThanksListDataBean extends ResponseBean {
    @SerializedName("data")
    protected ThanksAfterBean data;

    public ThanksAfterBean getData() {
        return data;
    }

    public void setData(ThanksAfterBean data) {
        this.data = data;
    }
}
