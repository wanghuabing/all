package com.longcheng.volunteer.modular.mine.goodluck.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.longcheng.volunteer.R;
import com.longcheng.volunteer.base.BaseAdapterHelper;
import com.longcheng.volunteer.modular.mine.goodluck.activity.OpenRedEnvelopeActivity;
import com.longcheng.volunteer.modular.mine.goodluck.bean.GoodLuckBean;
import com.longcheng.volunteer.utils.ConfigUtils;

import java.util.List;

/**
 * 作者：MarkShuai
 * 时间：2017/11/22 14:33
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public class GoodLuckAdapter extends BaseAdapterHelper<GoodLuckBean> {
    ViewHolder mHolder = null;

    Activity context;
    List<GoodLuckBean> starList;

    public GoodLuckAdapter(Activity context, List<GoodLuckBean> list, List<GoodLuckBean> starList) {
        super(context, list);
        this.context = context;
        this.starList = starList;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, List<GoodLuckBean> list, LayoutInflater inflater) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_goodluck_item, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.item_tv_mingxi.setVisibility(View.VISIBLE);
        GoodLuckBean mActionItemBean = list.get(position);
        mHolder.item_tv_num.setText("" + mActionItemBean.getPosition());
        mHolder.item_tv_name.setText("互祝红包奖励");
        mHolder.item_tv_shequ.setText(mActionItemBean.getCreate_time());
        int red_packet_status = mActionItemBean.getRed_packet_status();
        //红包状态: 0 未领取 , 1 已领取
        if (red_packet_status == 0) {
            mHolder.item_iv_open.setVisibility(View.VISIBLE);
            mHolder.item_tv_skb.setVisibility(View.GONE);
            mHolder.item_tv_mingxi.setVisibility(View.GONE);
        } else {
            mHolder.item_iv_open.setVisibility(View.GONE);
            mHolder.item_tv_skb.setVisibility(View.VISIBLE);
            mHolder.item_tv_mingxi.setVisibility(View.VISIBLE);
        }
        //红包类型 1:money, 2:skb, 3:ability, 4:goods
        int type = mActionItemBean.getType();
        String tilt = "";
        if (type == 1) {
            tilt = "现金";
        } else {
            tilt = "寿康宝";
        }
        mHolder.item_tv_skb.setText(mActionItemBean.getRed_packet_money() + tilt);
        mHolder.item_tv_mingxi.setText("已到账");

        mHolder.item_iv_open.setTag(mActionItemBean);
        mHolder.item_iv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starList != null && starList.size() > 0) {
                    int random = ConfigUtils.getINSTANCE().setRandom(starList.size() - 1);
                    GoodLuckBean mstarBean = starList.get(random);


                    GoodLuckBean mGoodLuckItemBean = (GoodLuckBean) v.getTag();
                    Intent intent = new Intent(context, OpenRedEnvelopeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("one_order_id", mGoodLuckItemBean.getOne_order_id());
                    intent.putExtra("position", mGoodLuckItemBean.getPosition());

                    Log.e("mGoodLuckItemBean", "" + mGoodLuckItemBean.toString());
                    Log.e("mstarBean", "" + mstarBean.toString());

                    intent.putExtra("type", mstarBean.getType());
                    intent.putExtra("url", mstarBean.getUrl());
                    intent.putExtra("qiming_user_id", mstarBean.getQiming_user_id());
                    intent.putExtra("action_goods_id", mstarBean.getAction_goods_id());
                    intent.putExtra("photo", mstarBean.getPhoto());
                    intent.putExtra("advertisement_photo_url", mstarBean.getAdvertisement_photo_url());
                    intent.putExtra("start_name", mstarBean.getStart_name());
                    intent.putExtra("slogan", mstarBean.getSlogan());

                    context.startActivity(intent);
                    ConfigUtils.getINSTANCE().setPageLoginIntentAnim(intent, context);
                }

            }
        });
        return convertView;
    }


    private class ViewHolder {
        private ImageView item_iv_open;
        private TextView item_tv_num;
        private TextView item_tv_name;
        private TextView item_tv_shequ;
        private TextView item_tv_skb;
        private TextView item_tv_mingxi;

        public ViewHolder(View view) {
            item_iv_open = (ImageView) view.findViewById(R.id.item_iv_open);
            item_tv_num = (TextView) view.findViewById(R.id.item_tv_num);
            item_tv_name = (TextView) view.findViewById(R.id.item_tv_name);
            item_tv_shequ = (TextView) view.findViewById(R.id.item_tv_shequ);
            item_tv_skb = (TextView) view.findViewById(R.id.item_tv_skb);
            item_tv_mingxi = (TextView) view.findViewById(R.id.item_tv_mingxi);
        }
    }
}
