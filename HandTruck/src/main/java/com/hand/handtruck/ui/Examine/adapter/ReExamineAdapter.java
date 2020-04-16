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
public class ReExamineAdapter extends BaseAdapter {

	private  List<FormBean> mList;
	private Context mContext;
	private LayoutInflater inflater;

	public ReExamineAdapter(Context context, List<FormBean> list){
		this.mContext = context;
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
		ViewHold holder = null;
		FormBean item = mList.get(position);
		if (convertView == null) {
			holder = new ViewHold();
			convertView =inflater.inflate(R.layout.act_all_unex, null);
			holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
			holder.tv_order_carnuber = (TextView) convertView.findViewById(R.id.tv_order_carnuber);
			holder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
			holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
			holder.tv_ji_tuan = (TextView) convertView.findViewById(R.id.tv_ji_tuan);
			holder.tv_pake_type=(TextView)convertView.findViewById(R.id.tv_pake_type);
			holder.tv_pake_weight=(TextView)convertView.findViewById(R.id.tv_pake_weight);
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
		TextView tv_order_number;
		TextView tv_order_carnuber;
		TextView tv_company_name;
		TextView tv_order_status;
		TextView tv_ji_tuan;

		TextView tv_pake_type;
		TextView tv_pake_weight;

	}
}
