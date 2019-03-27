package com.longcheng.volunteer.modular.home.healthydelivery.detail.activity;

import com.longcheng.volunteer.base.BaseModel;
import com.longcheng.volunteer.base.BasePresent;
import com.longcheng.volunteer.base.BaseView;
import com.longcheng.volunteer.modular.home.healthydelivery.list.bean.HealthyDeliveryResultBean;

/**
 * Created by Burning on 2018/9/13.
 */

public interface HealthyDeliveryContract {
    /**
     *
     */
    interface View extends BaseView<HealthyDeliveryContract.Presenter> {
        void onSuccess(HealthyDeliveryResultBean responseBean, int pageIndex);

        void onError(String msg);
    }

    abstract class Presenter<T> extends BasePresent<HealthyDeliveryContract.View> {
        abstract void doRefresh(int page, int pageSize, int type);
    }

    interface Model extends BaseModel {
    }
}
