package com.longcheng.lifecareplan.modular.mine.emergencys;

import com.longcheng.lifecareplan.base.BaseModel;
import com.longcheng.lifecareplan.base.BasePresent;
import com.longcheng.lifecareplan.base.BaseView;
import com.longcheng.lifecareplan.modular.mine.emergencys.bean.ApplyEmergencyBean;
import com.longcheng.lifecareplan.modular.mine.emergencys.bean.CertifyBean;
import com.longcheng.lifecareplan.modular.mine.emergencys.bean.EmergencyListBean;

public interface EmergencyContract {
    interface View extends BaseView<EmergencyContract.Presenter> {
        void ListSuccess(EmergencyListBean responseBean);

        void ApplySucess(ApplyEmergencyBean str);

        void getCard(CertifyBean str);

        void getCardError(String msg);

        void ListError();
    }

    abstract class Presenter<T> extends BasePresent<EmergencyContract.View> {
    }

    interface Model extends BaseModel {
    }
}