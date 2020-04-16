package com.hand.handtruck.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by wcf on 2018-03-08.
 * describe:首页车辆信息及汉德测重viewPager适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Fragment currentFragment,mFragment_truck_info,mFragment_truck_weight;


    public MainPagerAdapter(FragmentManager fm, Fragment mFragment_truckInfo,Fragment mFragment_truckWeight) {
        super(fm);
        this.mFragment_truck_info=mFragment_truckInfo;
        this.mFragment_truck_weight=mFragment_truckWeight;

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.currentFragment = (Fragment) object;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mFragment_truck_info;
            case 1:
                return mFragment_truck_weight;
        }
        return null;

    }


}
