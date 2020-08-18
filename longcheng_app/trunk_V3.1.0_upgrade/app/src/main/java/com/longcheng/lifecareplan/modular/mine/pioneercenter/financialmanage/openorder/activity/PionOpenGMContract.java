package com.longcheng.lifecareplan.modular.mine.pioneercenter.financialmanage.openorder.activity;

import com.longcheng.lifecareplan.base.BaseModel;
import com.longcheng.lifecareplan.base.BasePresent;
import com.longcheng.lifecareplan.base.BaseView;
import com.longcheng.lifecareplan.bean.ResponseBean;
import com.longcheng.lifecareplan.modular.mine.pioneercenter.financialmanage.openorder.bean.PionOpenGMListDataBean;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:06
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public interface PionOpenGMContract {
    interface View extends BaseView<Presenter> {
        void ListSuccess(PionOpenGMListDataBean responseBean, int back_page);

        void editSuccess(ResponseBean responseBean);

        void ListError();
    }

    abstract class Presenter<T> extends BasePresent<View> {
    }

    interface Model extends BaseModel {
    }
}