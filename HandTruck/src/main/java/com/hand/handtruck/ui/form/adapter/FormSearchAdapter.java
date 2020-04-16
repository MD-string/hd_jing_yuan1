package com.hand.handtruck.ui.form.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.ui.form.FormInfoActivity;
import com.hand.handtruck.ui.form.bean.FormBean;

import java.util.List;

import static com.hand.handtruck.application.MyApplication.context;

/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class FormSearchAdapter extends BaseAdapter {
     private Context mContext;
    private List<FormBean> mList;

    public FormSearchAdapter(Context mContext, List<FormBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void updateListView(List<FormBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (mList != null ){
            return mList.size();
        }else{
            return 0;
        }
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold holder;
        if (convertView == null) {
            holder = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_ist, null);
            holder.rl_1=(RelativeLayout)convertView.findViewById(R.id.rl_1);
            holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.tv_order_carnuber = (TextView) convertView.findViewById(R.id.tv_order_carnuber);
            holder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
            holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        final FormBean cb = mList.get(position);
        holder.tv_order_number.setText(cb.getOrderCode());
        holder.tv_order_carnuber.setText(cb.getCarNumber());
        holder.tv_company_name.setText(cb.getCustName());
        final String status=cb.getRailCheckStatus();//订单状态  0-未审核  1-正常卸货  2异常卸货 3设备异常 4数据异常

        if ("0".equals(status)){ //
            holder.tv_order_status.setText("在途");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
        }else if("2".equals(status)) {
            holder.tv_order_status.setText("异常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.hd_red));
        }else if("3".equals(status)){
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.green));
        }else if("4".equals(status)){
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBean bean=	cb;
                String status=bean.getRailCheckStatus();

//                if ("0".equals(status)){ //
//                    ToastUtil.getInstance().showCenterMessage(mContext, "该车辆处于在途状态，暂不提供数据查询");
//                }else{
                    FormInfoActivity.start(mContext,bean);
//                }
            }
        });
        return convertView;
    }

    class ViewHold {
        RelativeLayout rl_1;
        TextView tv_order_number;
        TextView tv_order_carnuber;
        TextView tv_company_name;
        TextView tv_order_status;
    }


}
