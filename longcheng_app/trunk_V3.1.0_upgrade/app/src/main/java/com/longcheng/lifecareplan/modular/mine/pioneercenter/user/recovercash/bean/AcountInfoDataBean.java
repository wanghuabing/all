package com.longcheng.lifecareplan.modular.mine.pioneercenter.user.recovercash.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.lifecareplan.bean.ResponseBean;

/**
 * 作者：jun on
 * 时间：2018/5/17 11:05
 * 意图：
 */

public class AcountInfoDataBean extends ResponseBean {
    @SerializedName("data")
    protected AcountAfterBean data;

    public AcountAfterBean getData() {
        return data;
    }

    public void setData(AcountAfterBean data) {
        this.data = data;
    }
}
