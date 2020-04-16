package com.hand.handtruck.ui.Examine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.ui.form.bean.FormBean;

import java.util.List;

/*
 * 
 */
public class ResearchAndColAdapter extends BaseAdapter {

	private  List<FormBean> mList;
	private Context context;
	private LayoutInflater inflater;

	public ResearchAndColAdapter(Context context, List<FormBean> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mList=list;
	}

	@Override
	public int getCount() {
		if (mList != null ){
			return mList.size();
		}else{
			return 0;
		}
	}

    public void updateListView(List<FormBean> list){
        this.mList = list;
        notifyDataSetChanged();
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
		ViewHold viewHold = null;
		FormBean item = mList.get(position);
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView =inflater.inflate(R.layout.activity_research_and_collection, null);
			viewHold.tv_cust_name=(TextView)convertView.findViewById(R.id.tv_cust_name);
			viewHold.tv_truck_number=(TextView)convertView.findViewById(R.id.tv_car_number);
			viewHold.tv_order_number=(TextView)convertView.findViewById(R.id.tv_order_number);
			viewHold.tv_start_weight=(TextView)convertView.findViewById(R.id.tv_start_weight);
            viewHold.tv_type_bao=(TextView)convertView.findViewById(R.id.tv_type_bao);
            viewHold.tv_go_out=(TextView)convertView.findViewById(R.id.tv_go_out);
			viewHold.tv_handler_advise=(TextView)convertView.findViewById(R.id.tv_handler_advise);
			viewHold.tv_logs_adv=(TextView)convertView.findViewById(R.id.tv_logs_adv);
			viewHold.tv_leader_adv=(TextView)convertView.findViewById(R.id.tv_leader_adv);
			viewHold.tv_boss_adv=(TextView)convertView.findViewById(R.id.tv_boss_adv);
			viewHold.tv_order_sta=(TextView)convertView.findViewById(R.id.tv_order_sta);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		if(item != null ){
			viewHold.tv_cust_name.setText(item.getCustName()+"");
			viewHold.tv_truck_number.setText(item.getCarNumber()+"");
			viewHold.tv_order_number.setText(item.getOrderCode()+"");
			viewHold.tv_start_weight.setText(item.getSendWeight()+" 吨");
			viewHold.tv_type_bao.setText(item.getPackType()+"");
			viewHold.tv_go_out.setText(item.getLeaveTime()+"");
			viewHold.tv_handler_advise.setText("意见");
			String status=item.getRailCheckStatus();//0未审核  1正常卸货、2异常卸货  3设备异常  4.数据异常 5.未配置围栏
			if("0".equals(status) ){
				viewHold.tv_order_sta.setText("未审核");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.green));
			}else if("1".equals(status) ){
				viewHold.tv_order_sta.setText("正常卸货");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.black));
			}else if("2".equals(status)){
				viewHold.tv_order_sta.setText("异常卸货");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.hd_red));
			}else if("3".equals(status)){
				viewHold.tv_order_sta.setText("正常卸货");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.black));
			}else if("4".equals(status)){
				viewHold.tv_order_sta.setText("正常卸货");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.black));
			}else if("5".equals(status)){
				viewHold.tv_order_sta.setText("未配置围栏");
				viewHold.tv_order_sta.setTextColor(context.getResources().getColor(R.color.gg_99));
			}

		}
		return convertView;
	}

	class ViewHold {
		TextView tv_cust_name;
		TextView tv_order_sta;
		TextView tv_truck_number;
        TextView tv_order_number;
		TextView tv_start_weight;
		TextView tv_type_bao;
		TextView tv_go_out;
		TextView tv_handler_advise;
		TextView tv_logs_adv;
		TextView tv_leader_adv;
		TextView tv_boss_adv;
	}
}
