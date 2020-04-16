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
public class PicLeaderExAdapter extends BaseAdapter {

	private  List<FormBean> mList;
	private Context context;
	private LayoutInflater inflater;

	public PicLeaderExAdapter(Context context, List<FormBean> list){
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
			convertView =inflater.inflate(R.layout.act_picleader_ex, null);
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
			String adsice=item.getLevel1CheckMsg();
			viewHold.tv_handler_advise.setText(adsice+"");

			String status=item.getLevel1CheckStatus();  //物流部审核状态0-未审核1-审核通过2审核不通过
			if("2".equals(status)){
				viewHold.tv_logs_adv.setText("不通过");
				viewHold.tv_logs_adv.setTextColor(context.getResources().getColor(R.color.hd_red));
			}else if("1".equals(status)){
				viewHold.tv_logs_adv.setText("通过");
				viewHold.tv_logs_adv.setTextColor(context.getResources().getColor(R.color.green));
			}else{
				viewHold.tv_logs_adv.setText("未审核");
				viewHold.tv_logs_adv.setTextColor(context.getResources().getColor(R.color.main_yellow));
			}

			String adsice1=item.getLevel2CheckMsg();
			viewHold.tv_handler_advise.setText(adsice1+"");

			String status1=item.getLevel2CheckStatus();  //分管领导审核状态0-未审核1-审核通过2审核不通过
			if("2".equals(status1)){
				viewHold.tv_leader_adv.setText("不通过");
				viewHold.tv_leader_adv.setTextColor(context.getResources().getColor(R.color.hd_red));
			}else if("1".equals(status1)){
				viewHold.tv_leader_adv.setText("通过");
				viewHold.tv_leader_adv.setTextColor(context.getResources().getColor(R.color.green));
			}else{
				viewHold.tv_leader_adv.setText("未审核");
				viewHold.tv_leader_adv.setTextColor(context.getResources().getColor(R.color.main_yellow));
			}

		}
		return convertView;
	}

	class ViewHold {
		TextView tv_cust_name;
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
