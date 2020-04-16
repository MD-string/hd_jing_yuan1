package com.hand.handtruck.ui.TruckInfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.ui.TruckInfo.WeightTrendMainActivity;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;

import java.io.Serializable;
import java.util.List;

import static com.hand.handtruck.R.id.tv_truck_isOnLine;
import static com.hand.handtruck.application.MyApplication.context;

/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class CarNumberAdapter extends BaseAdapter {
     private Context mContext;
    private List<CarInfo> mList;

    public CarNumberAdapter(Context mContext, List<CarInfo> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_truck, null);
            holder.rl_1=(RelativeLayout)convertView.findViewById(R.id.rl_1);
            holder.iv_isOnLine = (ImageView) convertView.findViewById(R.id.iv_isOnLine);
            holder.tv_plate_number = (TextView) convertView.findViewById(R.id.tv_plate_number);
            holder.tv_truck_isOnLine = (TextView) convertView.findViewById(tv_truck_isOnLine);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        final CarInfo cb = mList.get(position);
        holder.tv_plate_number.setText(cb.getText());

        String status=cb.getIcon();
        if(status.contains("online")){//在线
            holder.tv_truck_isOnLine.setText("在线");
            holder.iv_isOnLine.setSelected(false);
            holder.tv_truck_isOnLine.setSelected(false);
        }else{
            holder.tv_truck_isOnLine.setText("离线");
            holder.iv_isOnLine.setSelected(true);
            holder.tv_truck_isOnLine.setSelected(true);
        }
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status=cb.getIcon();
                String str="";
                if(status.contains("online")){//在线
                    str="在线";
                }else{
                    str="离线";
                }
                cb.setCarNumber(cb.getText()+"");
                cb.setDeviceId(cb.getId()+"");
                Bundle bundle=new Bundle();
                bundle.putSerializable("truckModel",(Serializable)cb);
                bundle.putString("sign",str);
                //                CommonKitUtil.startActivity((Activity) mContext, WeightTrendActivity.class, bundle, false);
                CommonKitUtil.startActivity((Activity) mContext, WeightTrendMainActivity.class, bundle, false);
            }
        });
        return convertView;
    }

    class ViewHold {
        RelativeLayout rl_1;
        ImageView iv_isOnLine;
        TextView tv_plate_number;
        TextView tv_truck_isOnLine;
    }


}
