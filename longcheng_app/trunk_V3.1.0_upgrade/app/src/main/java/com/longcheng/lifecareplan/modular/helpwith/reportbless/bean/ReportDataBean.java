package com.longcheng.lifecareplan.modular.helpwith.reportbless.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.lifecareplan.bean.ResponseBean;

/**
 * 作者：jun on
 * 时间：2018/5/17 11:05
 * 意图：
 */

public class ReportDataBean extends ResponseBean {
    @SerializedName("data")
    protected ReportAfterBean data;

    public ReportAfterBean getData() {
        return data;
    }

    public void setData(ReportAfterBean data) {
        this.data = data;
    }
}