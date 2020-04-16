package com.hand.handtruck.ui.TruckInfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.ui.TruckInfo.CTruckSecondActivity;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;

import java.io.Serializable;
import java.util.List;

import static com.hand.handtruck.application.MyApplication.context;

/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class CompanyAdapter extends BaseAdapter {
     private Context mContext;
    private List<CarInfo> mList;

    public CompanyAdapter(Context mContext, List<CarInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void updateListView(List<CarInfo> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_company1, null);
            holder.rl_1 = (RelativeLayout) convertView.findViewById(R.id.rl_1);
            holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
            holder.tv_truck_number = (TextView) convertView.findViewById(R.id.tv_truck_number);
            holder.tv_online_truck_number = (TextView) convertView.findViewById(R.id.tv_online_truck_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        final CarInfo cb = mList.get(position);
        holder.tv_company.setText(cb.getText());
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putSerializable("sencond_info", (Serializable) cb);
                    CommonKitUtil.startActivity((Activity) mContext, CTruckSecondActivity.class, bund, false);
            }
        });
        return convertView;

    }
    class ViewHold {
        RelativeLayout rl_1;
        TextView tv_company;
        TextView tv_truck_number;
        TextView tv_online_truck_number;
    }


}
