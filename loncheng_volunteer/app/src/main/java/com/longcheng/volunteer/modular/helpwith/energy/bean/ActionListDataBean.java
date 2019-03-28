package com.longcheng.volunteer.modular.helpwith.energy.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.volunteer.bean.ResponseBean;

import java.util.List;

/**
 * 作者：jun on
 * 时间：2018/8/21 13:12
 * 意图：返回对象
 */

public class ActionListDataBean extends ResponseBean {
    @SerializedName("data")
    private List<ActionItemBean> data;

    public List<ActionItemBean> getData() {
        return data;
    }

    public void setData(List<ActionItemBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "data =" + data + " , msg = " + msg + " , status = " + status;
    }
}