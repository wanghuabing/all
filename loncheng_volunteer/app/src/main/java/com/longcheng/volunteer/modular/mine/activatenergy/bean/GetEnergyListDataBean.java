package com.longcheng.volunteer.modular.mine.activatenergy.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.volunteer.bean.ResponseBean;

/**
 * 作者：jun on
 * 时间：2018/8/21 13:12
 * 意图：返回对象
 */

public class GetEnergyListDataBean extends ResponseBean {
    @SerializedName("data")
    private EnergyAfterBean data;

    public EnergyAfterBean getData() {
        return data;
    }

    public void setData(EnergyAfterBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "data =" + data + " , msg = " + msg + " , status = " + status;
    }
}
