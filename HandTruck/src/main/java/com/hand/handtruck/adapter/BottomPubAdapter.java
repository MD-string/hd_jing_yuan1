package com.hand.handtruck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hand.handtruck.R;

import java.util.List;

/*
 * 
 */
public class BottomPubAdapter extends BaseAdapter {

	private final List<String> mList;
	private Context context;
	private LayoutInflater inflater;

	public BottomPubAdapter(Context context, List<String> list){
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
		String item = mList.get(position);
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView =inflater.inflate(R.layout.activity_bottom_pub, null);
			viewHold.tv_1=(TextView)convertView.findViewById(R.id.tv_1);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		if(item != null ){
			viewHold.tv_1.setText(item);
		}
		return convertView;
	}

	class ViewHold {
		TextView tv_1;
	}
}
