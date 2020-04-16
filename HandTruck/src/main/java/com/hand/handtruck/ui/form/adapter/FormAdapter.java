package com.hand.handtruck.ui.form.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.utils.Tools;

import java.util.List;

import static com.hand.handtruck.application.MyApplication.context;

/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class FormAdapter extends BaseAdapter {
     private Context mContext;
    private List<FormBean> mList;

    public FormAdapter(Context mContext, List<FormBean> mList) {
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
            holder.tv_ji_tuan = (TextView) convertView.findViewById(R.id.tv_ji_tuan);
            holder.tv_pake_type=(TextView)convertView.findViewById(R.id.tv_pake_type);
            holder.tv_pake_weight=(TextView)convertView.findViewById(R.id.tv_pake_weight);
            holder.tv_out_date=(TextView)convertView.findViewById(R.id.tv_out_date);
            holder.tv_end_date=(TextView)convertView.findViewById(R.id.tv_end_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        final FormBean cb = mList.get(position);
        holder.tv_order_number.setText(cb.getOrderCode());
        holder.tv_order_carnuber.setText(cb.getCarNumber());
        holder.tv_company_name.setText(cb.getCustName());
        holder.tv_ji_tuan.setText(cb.getFactoryName());
        holder.tv_pake_type.setText(cb.getPackType()+"");
        holder.tv_pake_weight.setText(cb.getSendWeight()+"吨");
        holder.tv_out_date.setText(cb.getLeaveTime()+"");
        String endTime=cb.getUnloadEndTime();
        if(Tools.isEmpty(endTime)){
            endTime="";
        }
        holder.tv_end_date.setText(endTime);
       final String status=cb.getRailCheckStatus();//订单状态  0-未审核  1-正常卸货  2异常卸货 3设备异常 4数据异常 5,未配置围栏

        if ("0".equals(status)){ //
            holder.tv_order_status.setText("运输中");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_blue));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_blue));
        }else if("2".equals(status)) {
            holder.tv_order_status.setText("异常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_red));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_red));
        }else if("3".equals(status)){
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_green));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }else if("4".equals(status)){
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_green));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }else if("5".equals(status)){
            holder.tv_order_status.setText("未配围栏");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_yellow));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_yellow));
        }
        else {
            holder.tv_order_status.setText("正常卸货");
            holder.tv_order_status.setTextColor(mContext.getResources().getColor(R.color.gh_green));
            holder.tv_order_carnuber.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }
        return convertView;
    }

    class ViewHold {
        RelativeLayout rl_1;
        TextView tv_order_number;
        TextView tv_order_carnuber;
        TextView tv_company_name;
        TextView tv_order_status;
        TextView tv_ji_tuan;

        TextView tv_pake_type;
        TextView tv_pake_weight;
        TextView tv_out_date;
        TextView tv_end_date;
    }


}
