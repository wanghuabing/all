package com.longcheng.lifecareplan.modular.helpwith.medalrank.activity;

import com.longcheng.lifecareplan.base.BaseModel;
import com.longcheng.lifecareplan.base.BasePresent;
import com.longcheng.lifecareplan.base.BaseView;
import com.longcheng.lifecareplan.modular.helpwith.medalrank.bean.MyRankListDataBean;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:06
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public interface MyContract {
    interface View extends BaseView<Presenter> {
        void PersonalListSuccess(MyRankListDataBean responseBean, int back_page);

        void CommuneListSuccess(MyRankListDataBean responseBean, int back_page);

        void ListError();
    }

    abstract class Presenter<T> extends BasePresent<View> {
    }

    interface Model extends BaseModel {
    }
}
