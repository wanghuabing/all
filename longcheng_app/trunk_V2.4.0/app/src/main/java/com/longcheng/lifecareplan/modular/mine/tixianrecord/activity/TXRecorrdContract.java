package com.longcheng.lifecareplan.modular.mine.tixianrecord.activity;

import com.longcheng.lifecareplan.base.BaseModel;
import com.longcheng.lifecareplan.base.BasePresent;
import com.longcheng.lifecareplan.base.BaseView;
import com.longcheng.lifecareplan.modular.mine.tixianrecord.bean.TXRecordDataBean;
import com.longcheng.lifecareplan.modular.mine.userinfo.bean.EditListDataBean;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:06
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public interface TXRecorrdContract {
    interface View extends BaseView<Presenter> {
        void ListSuccess(TXRecordDataBean responseBean, int back_page);


        void editSuccess(EditListDataBean responseBean);


        void ListError();
    }

    abstract class Presenter<T> extends BasePresent<View> {
    }

    interface Model extends BaseModel {
    }
}