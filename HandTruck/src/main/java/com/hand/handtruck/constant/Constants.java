package com.hand.handtruck.constant;


import com.hand.handtruck.application.MyApplication;

/**
 * Created by wcf on 2018/3/20.
 */
public class Constants {
    public static class HttpUrl {

        /*权限，post*/
        public static final String URL_POWER = GET_URL() + "/authorityServer/getUserAuthorityList.html";

        /*，获取该用户的车辆列表,POST */
        public static final String CAR_LIST = GET_URL() + "/carserver/carlist.html";

        /*获取该车辆列表实时信息  POST*/
        public static final String CAR_REAL_TIME_LIST = GET_URL() + "/carserver/realtimelist.html";
        /*根据设备编号、时间区间查询该设备编号的载重数据  POST*/
        public static final String CAR_WEIGHT_DATA = GET_URL() + "/dataserver/weightdata.html";
        /*根据设备编号、时间区间查询该设备编号的载重数据 含经纬度  POST*/
        public static final String CAR_GPS_DATA = GET_URL() + "/dataserver/gpsdata.html";
        /*获取公司列表信息  POST*/
        public static final String COMPANY_LIST = GET_URL() + "/companyserver/companylist.html";
        /*添加或修改车辆信息接口接口功能录入或修改车辆信息 POST*/
        public static final String CAR_ADD_EDITOR = GET_URL() + "/carserver/addoredit.html";
        /*获取省份列表信息*/
        public static final String PROVINCELIST_LIST_ = GET_URL() + "/companyserver/provincelist.html";
        /*根据省份ID获取城市列表信息*/
        public static final String CITY_LIST = GET_URL() + "/companyserver/citylist.html";
        /*添加或修改公司信息接口*/
        public static final String COMPANY_ADD_EDITOR  = GET_URL() + "/companyserver/addoredit.html";
        /*根据用户登录凭证和设备ID，获取该车辆状态信息即车辆在线信息及修改*/
        public static final String CAR_STATUS  = GET_URL() + "/carserver/status.html";

        /*添加或修改公司信息接口  POST*/
        public static final String COMPANY_CHANGE_LIST = GET_URL() +"/companyserver/addoredit.html";
        /*获取省份  POST*/
        public static final String PROVINCE_LIST = GET_URL() + "/companyserver/provincelist.html ";

        /*行政区域查询API服务地址T*/
        public static final String WEI_LAN = "https://restapi.amap.com/v3/config/district";

        /*运输任务列表接口  POST*/
        public static final String TRANSPORT_LIST = GET_URL()  + "/alarmServer/powerOffAlarmList.html";


        /*获取用户角色资源列表接口 权限  POST*/
        public static final String USER_POWER = GET_URL()  + "/resourceserver/resourceList.html";


        //////////////////////////////////

        /*登录，post*/
        public static final String LOGIN = GET_URL() + "/loginserver/login.html";

        /*、车辆实时数据列表接口  POST*/
        public static final String GET_CAR_LIST = GET_URL()  + "/dataserver/realtimelist.html";
//
//        /*、设备离线报警数据列表接口   POST*/
//        public static final String OUT_LINE = GET_URL()  + "/alarmServer/offLineAlarmList.html";
//        /*、设备关机报警数据列表接口   POST*/
//        public static final String CLOSE_ALARM = GET_URL()  + "/alarmServer/powerOffAlarmList.html";
        /*、设备卸货异常数据列表接口   POST*/
        public static final String UN_LOAD = GET_URL()  + "/alarmServer/unloadAlarmList.html";

        public static final String Error_ALARM = GET_URL()  + "/alarmServer/queryDeviceAlarmList.html";

        /*获取该车辆实时信息  POST*/
        public static final String CAR_REAL_TIME = GET_URL() + "/dataserver/realtime.html";

        /*历史数据接口  POST*/
        public static final String HISTORY_LIST = GET_URL() + "/dataserver/datalist.html";
        /*获取订单列表   POST*/
        public static final String ORDER_LIST = GET_URL() + "/orderserver/orderlist.html";

        public static final String ORDER_LIST1 = GET_URL() + "/orderserver/queryNormalAndExceptionOrderList.html";//金圆 不需要railCheckStatus和orderCheckStatus参数

        /*、获取订单围栏    POST*/
        public static final String ORDER_RAIL = GET_URL() + "/orderserver/getRailListByOrderId.html";



        /*、订单人工调研审核   POST*/
        public static final String CHECK_ORDER = GET_URL() + "/orderserver/checkOrderManual.html";

        /*、、订单审核(二级:物流部审核、三级:分管领导审核、四级:总经理审核)POST*/
        public static final String CHECK_MORE = GET_URL() + "/orderserver/checkOrder.html";


        /*、、订单审核(二级:物流部审核、三级:分管领导审核、四级:总经理审核)POST*/
        public static final String CHECK_MORE1 = GET_URL() + "/jy/Orderserver/checkOrder.html"; //金圆

        /*车辆列表*/
        public static final String COMPANY_LIST_CAR = GET_URL() + "/carserver/devicelist.html";

        /*根据订单编号获取订单信息*/
        public static final String TRANS_BY_ORDER_CODE = GET_URL() + "/orderserver/getTransByOrderCode.html";
    }

    public static String GET_URL() {
        String url = "";
        switch (MyApplication.environment) {
            case 0:
                url = "http://192.168.10.43:8089/api/V1";//测试环境
                break;
            case 1:
                url = "http://39.108.114.33:8088/api/V1";//生产环境
                break;

        }
        return url;
    }

}

