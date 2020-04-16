package com.hand.handtruck.Widget.sortlistviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.bean.CompanyTruckGroupBean;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {

	private List<CompanyTruckGroupBean> mlist = null;

	private Context mContext;

	public SortAdapter(Context mContext,List<CompanyTruckGroupBean> list){
		this.mContext = mContext;
		this.mlist = list;
	}
	public void updateListView(List<CompanyTruckGroupBean> list){
		this.mlist = list;
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return this.mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		final CompanyTruckGroupBean mContent = mlist.get(position);
		if (convertView== null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_company, null);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			viewHolder.line_1=(TextView)convertView.findViewById(R.id.line_1);
			viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
			viewHolder.iv_truckInfo_arrow = (ImageView) convertView.findViewById(R.id.iv_truckInfo_arrow);
			viewHolder.tv_truck_number = (TextView) convertView.findViewById(R.id.tv_truck_number);
			viewHolder.tv_online_truck_number = (TextView) convertView.findViewById(R.id.tv_online_truck_number);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getLetters());
		}else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		if(0==position){
			viewHolder.line_1.setVisibility(View.GONE);
		}else{
			String first= mlist.get(position).getLetters();
			String fletter=getAlpha(first);
			String mfist=mlist.get(position-1).getLetters();
			String mletter=getAlpha(mfist);
			if(fletter.equals(mletter)){
				viewHolder.line_1.setVisibility(View.VISIBLE);
			}else{
				viewHolder.line_1.setVisibility(View.GONE);
			}
		}
		viewHolder.tv_company.setText(mlist.get(position).getName());
		viewHolder.tv_truck_number.setText(mlist.get(position).getTruckNumber()+"");
		viewHolder.tv_online_truck_number.setText(mlist.get(position).getOnlineNumber()+"");
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mlist.get(i).getLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return mlist.get(position).getLetters().charAt(0);
	}


	final static class ViewHolder{
		TextView line_1;
		TextView tv_company;
		TextView tv_truck_number;
		TextView tv_online_truck_number;
		ImageView iv_truckInfo_arrow;
		TextView tvLetter;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}


}
