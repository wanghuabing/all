package com.longcheng.lifecareplan.modular.helpwith.lifestyledetail.lifestylerank.activity;

import com.longcheng.lifecareplan.base.BaseModel;
import com.longcheng.lifecareplan.base.BasePresent;
import com.longcheng.lifecareplan.base.BaseView;
import com.longcheng.lifecareplan.modular.helpwith.lifestyledetail.lifestylerank.bean.LifeRankListDataBean;

/**
 * 作者：MarkShuai
 * 时间：2017/12/8 16:06
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public interface LifeRankContract {
    interface View extends BaseView<Presenter> {
        void ListSuccess(LifeRankListDataBean responseBean, int page);

        void ListError();
    }

    abstract class Presenter<T> extends BasePresent<View> {
    }

    interface Model extends BaseModel {
    }
}
