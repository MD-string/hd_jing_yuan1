package com.hand.handtruck.ui.TruckInfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.ui.TruckInfo.WeightTrendMainActivity;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.bean.CompanyInfo;

import java.io.Serializable;
import java.util.List;

import static com.hand.handtruck.R.id.tv_truck_isOnLine;
import static com.hand.handtruck.application.MyApplication.context;

/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class CompanyTruckAdapter2 extends BaseExpandableListAdapter {
     private Context mContext;
    private List<CompanyInfo> mList;

    public CompanyTruckAdapter2(Context mContext, List<CompanyInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void updateListView(List<CompanyInfo> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int size=mList.size();
        if(i >= size){
             return mList.get(size-1).getCarList().size();
        }else{
            List<CarInfo> list= mList.get(i).getCarList();
            if(list !=null && list.size() >0){

                return mList.get(i).getCarList().size();
            }else{
                return  0;
            }
        }
    }

    @Override
    public Object getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mList.get(i).getCarList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder holder;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(context).inflate(R.layout.lvitem_company1, null);
            holder.line_1=(TextView)view.findViewById(R.id.line_1);
            holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
            holder.iv_truckInfo_arrow = (ImageView ) view.findViewById(R.id.iv_truckInfo_arrow);
            holder.tv_truck_number = (TextView) view.findViewById(R.id.tv_truck_number);
          holder.tv_online_truck_number = (TextView) view.findViewById(R.id.tv_online_truck_number);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        holder.tv_company.setText(mList.get(i).getName());
        holder.tv_truck_number.setText(mList.get(i).getCarList().size()+"");
        holder.tv_online_truck_number.setText(mList.get(i).getOnLineNumber()+"");
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(context).inflate(R.layout.lvitem_truck_1, null);
            holder.rl_1=(RelativeLayout)view.findViewById(R.id.rl_1);
            holder.iv_isOnLine = (ImageView) view.findViewById(R.id.iv_isOnLine);
            holder.tv_plate_number = (TextView) view.findViewById(R.id.tv_plate_number);
            holder.tv_truck_isOnLine = (TextView) view.findViewById(tv_truck_isOnLine);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
       final CarInfo cb = mList.get(i).getCarList().get(i1);
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
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {


        return true;
    }

    class GroupHolder {
        TextView tv_company,tv_truck_number,tv_online_truck_number,line_1;
        ImageView iv_truckInfo_arrow;
    }

    class ChildHolder {
        ImageView iv_isOnLine;
        TextView tv_plate_number, tv_truck_isOnLine;
        RelativeLayout rl_1;
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
