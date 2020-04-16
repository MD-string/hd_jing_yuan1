package com.hand.handtruck.ui.TruckInfo;

import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;

import java.util.List;

/**
 * 动态接口定义
 */
public interface ICarListView {
	void doSuccess( List<CarInfo> msg);
	void doError(String msg);

}
