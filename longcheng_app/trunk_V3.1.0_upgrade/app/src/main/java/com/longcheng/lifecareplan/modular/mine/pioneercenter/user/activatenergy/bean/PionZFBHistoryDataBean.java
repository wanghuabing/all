package com.longcheng.lifecareplan.modular.mine.pioneercenter.user.activatenergy.bean;

import com.google.gson.annotations.SerializedName;
import com.longcheng.lifecareplan.bean.ResponseBean;

import java.util.ArrayList;

/**
 * 作者：jun on
 * 时间：2018/5/17 11:05
 * 意图：
 */

public class PionZFBHistoryDataBean extends ResponseBean {
    @SerializedName("data")
    protected PionZFBSBean data;

    public PionZFBSBean getData() {
        return data;
    }

    public void setData(PionZFBSBean data) {
        this.data = data;
    }

    public class PionZFBSBean {
        int listType; // 1 历史 2 推荐
        ArrayList<PionZFBSBean> entreList;
        String phone;
        String user_name;
        String avatar;
        String jieqi_name;
        String entrepreneurs_id;

        public int getListType() {
            return listType;
        }

        public void setListType(int listType) {
            this.listType = listType;
        }

        public ArrayList<PionZFBSBean> getEntreList() {
            return entreList;
        }

        public void setEntreList(ArrayList<PionZFBSBean> entreList) {
            this.entreList = entreList;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getJieqi_name() {
            return jieqi_name;
        }

        public void setJieqi_name(String jieqi_name) {
            this.jieqi_name = jieqi_name;
        }

        public String getEntrepreneurs_id() {
            return entrepreneurs_id;
        }

        public void setEntrepreneurs_id(String entrepreneurs_id) {
            this.entrepreneurs_id = entrepreneurs_id;
        }
    }
}
