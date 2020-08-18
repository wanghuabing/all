package com.longcheng.lifecareplan.modular.helpwith.energydetail.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.lifecareplan.bean.ResponseBean;

import java.util.List;

/**
 * 作者：jun on
 * 时间：2018/8/21 13:12
 * 意图：返回对象
 */

public class CommentDataBean extends ResponseBean {
    @SerializedName("data")
    private DetailItemBean data;

    public DetailItemBean getData() {
        return data;
    }

    public void setData(DetailItemBean data) {
        this.data = data;
    }
}