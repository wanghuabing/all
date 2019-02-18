package com.longcheng.lifecareplan.modular.helpwith.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longcheng.lifecareplan.R;
import com.longcheng.lifecareplan.base.BaseAdapterHelper;
import com.longcheng.lifecareplan.modular.helpwith.bean.HelpWithInfo;
import com.longcheng.lifecareplan.utils.DensityUtil;

import java.util.List;

/**
 * 作者：MarkShuai
 * 时间：2017/11/22 14:33
 * 邮箱：MarkShuai@163.com
 * 意图：
 */

public class HelpWithTopAdapter extends BaseAdapterHelper<HelpWithInfo> {
    ViewHolder mHolder = null;

    Context context;

    public HelpWithTopAdapter(Context context, List<HelpWithInfo> list) {
        super(context, list);
        this.context = context;
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, List<HelpWithInfo> list, LayoutInflater inflater) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_helpwith_topitem, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        HelpWithInfo mHelpWithInfo = list.get(position);
        mHolder.item_tv_cont.setText(mHelpWithInfo.getName());
        mHolder.item_iv_select.setBackgroundResource(mHelpWithInfo.getImgId());

        int width = (DensityUtil.screenWith(context) - DensityUtil.dip2px(context, 30)) / 2;
        int height = (int) (width * 0.588);
        mHolder.item_iv_select.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        return convertView;
    }


    private class ViewHolder {
        private TextView item_tv_cont;
        private ImageView item_iv_select;

        public ViewHolder(View view) {
            item_tv_cont = (TextView) view.findViewById(R.id.item_tv_cont);
            item_iv_select = (ImageView) view.findViewById(R.id.item_iv_select);
        }
    }
}
