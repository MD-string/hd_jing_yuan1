package com.hand.handtruck.activity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.TextUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CompanyResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.CompanyListTasks;
import com.hand.handtruck.domain.TruckAddTask;
import com.hand.handtruck.model.CompanyBean;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.utils.LogUtil;
import com.parkingwang.vehiclekeyboard.support.PopupKeyboard;
import com.parkingwang.vehiclekeyboard.view.InputView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * describe:添加车辆页
 */
public class AddTruckActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlTitle;
    private LinearLayout ll_select_company;
    private TextView mTvSelectCompany;
    private EditText mEtDeviceNumber;
    private EditText mEtGpsNumber;
    private EditText mEtPhone;
    private EditText mEtLoadCapacity;
    private TextView mTvProductionTime,mTvOkTruckN,mTvCancelTruckN,mEtTruckNumber;
    private Button mBtnAddTruck;
    private CompanyTruckBean truckModel;
    private Activity mContext;
    private TimePickerView pvCustomTime;
    private String companyId="";
    private String token;
    private TruckAddTask truckAddTask;
    private CompanyListTasks companyTruckTask;
    private OptionsPickerView pvCustomOptions;
    private List<CompanyBean> companyList=new ArrayList<>();
    private InputView mInputView;
    private PopupKeyboard mPopupKeyboard;
    private LinearLayout dialog_truckN_input;
    private LinearLayout ll_all;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取公司
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    LogUtil.e("添加车辆中获取公司列表成功");
                    CompanyResultBean companyResult = (CompanyResultBean) msg.obj;
                    companyList = companyResult.getResult();
                    pvCustomOptions.setPicker(companyList);//添加数据
                    break;
                //获取公司失败
                case ConstantsCode.MSG_REQUEST_FAIL1:

                    break;
                  /*添加车辆信息成功*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    LogUtil.e("添加车辆成功");

                    break;
                //添加车辆信息失败
                case ConstantsCode.MSG_REQUEST_FAIL:

                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_truck_add;
    }

    @Override
    protected void findViews() {
        ll_all=(LinearLayout)findViewById(R.id.ll_all);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("添加车辆");

        ll_select_company=(LinearLayout) findViewById(R.id.ll_select_company);
        mTvSelectCompany = (TextView) findViewById(R.id.et_select_company);
        mEtDeviceNumber = (EditText)findViewById(R.id.et_device_number);
        mEtGpsNumber = (EditText) findViewById(R.id.et_gps_number);
        mEtTruckNumber = (TextView) findViewById(R.id.tv_truck_number);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtLoadCapacity = (EditText) findViewById(R.id.et_load_capacity);
        mTvProductionTime = (TextView) findViewById(R.id.et_production_time);
        mBtnAddTruck = (Button) findViewById(R.id.btn_add_truck);
        dialog_truckN_input = (LinearLayout)findViewById(R.id.dialog_truckN_input);
        dialog_truckN_input.setVisibility(View.GONE);
        mInputView = (InputView) findViewById(R.id.input_view);
        mTvOkTruckN = (TextView) findViewById(R.id.tv_okTruckN);
        mTvCancelTruckN = (TextView)findViewById(R.id.tv_cancelTruckN);

        TextView  tv_cancle=(TextView)findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_truckN_input.setVisibility(View.GONE);
                mInputView.performFirstItem();
                mInputView.updateNumber("");
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(mContext);
                }
            }
        });

    }


    @Override
    protected void setListeners() {
        ll_select_company.setOnClickListener(this);//选择公司*/
        mTvProductionTime.setOnClickListener(this);
        mBtnAddTruck.setOnClickListener(this);
        mEtTruckNumber.setOnClickListener(this);
        mTvOkTruckN.setOnClickListener(this);
        mTvCancelTruckN.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        mContext=AddTruckActivity.this;
        mPopupKeyboard = new PopupKeyboard(AddTruckActivity.this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, AddTruckActivity.this);
        SharedPreferences sp = getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        companyTruckTask = new CompanyListTasks(mContext, mHandler);
        initCompanyList();
        initCustomOptionPicker();
        initCustomTimePicker();

    }

  
    /*获取公司列表信息*/
    private void initCompanyList() {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        companyTruckTask.getCompanyList(mapParams);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*选择公司*/
            case R.id.ll_select_company:
                hideKeyBorad();
                pvCustomOptions.show(); //弹出自定义条件选择器
                break;
              /*选择生产日期*/
            case R.id.et_production_time:
                hideKeyBorad();
                pvCustomTime.show();
                break;
              /*添加车辆*/
            case R.id.btn_add_truck:
                hideKeyBorad();
                submit();
                break;
            /*输入车牌号*/
            case R.id.tv_truck_number:
                hideKeyBorad();
                dialog_truckN_input.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加上这句才会弹出键盘
                        mInputView.performFirstItem();
                        //必须延时显示，否则车牌键盘被顶上去，即使系统键盘已经被隐藏，放到popWindow，dialog都无用
                        mPopupKeyboard.show(mContext);
                    }
                }, 100);

                break;
            /*确定车牌号*/
            case R.id.tv_okTruckN:
                if (TextUtils.isEmpty(mInputView.getNumber().toString())) {
                    Toast.makeText(mContext, "车牌号码不能为空", Toast.LENGTH_SHORT).show();

                    return;
                } else if (mInputView.getNumber().length() < 7) {
                    Toast.makeText(mContext, "请输入正确的车牌号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                // mCarNumber = mInputView.getNumber();
                mEtTruckNumber.setText(mInputView.getNumber() + "");
                dialog_truckN_input.setVisibility(View.GONE);
                mInputView.updateNumber("");
                mInputView.performFirstItem();
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(mContext);
                }

                break;
            case R.id.tv_cancelTruckN:
                dialog_truckN_input.setVisibility(View.GONE);
                mInputView.performFirstItem();
                mInputView.updateNumber("");
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(mContext);
                }

                break;
        }
    }
    private void hideKeyBorad() {
        InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mTvProductionTime.setText(getTime(date));
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.6f)
                .setTextColorCenter(0xFF0E82EB)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                companyId= companyList.get(options1).getId();
                LogUtil.e("公司companyId=="+companyId);
                String tx = companyList.get(options1).getCompanyName();
                mTvSelectCompany.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);

                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .isDialog(true)
                .setLineSpacingMultiplier(1.7f)
                .setTextColorCenter(0xFF0E82EB)
                .build();

        pvCustomOptions.setPicker(companyList);//添加数据

    }

    private void submit() {
        // validate
        if (!CommonKitUtil.isNetworkAvailable(mContext)) {
            ToastUtil.showMsgShort(mContext, ConstantsCode.NETWORK_ERROR);
            return;
        }
        String company = mTvSelectCompany.getText().toString().trim();
        if (TextUtils.isEmpty(company)) {
            Toast.makeText(mContext, "公司名称不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.focusView(mTvSelectCompany);
            return;
        }

        String deviceNumber = mEtDeviceNumber.getText().toString().trim();
        if (TextUtils.isEmpty(deviceNumber)) {
            Toast.makeText(mContext, "设备编号不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true, mEtDeviceNumber);
            return;
        }

        String gpsNumber = mEtGpsNumber.getText().toString().trim();
        if (TextUtils.isEmpty(gpsNumber)) {
            Toast.makeText(mContext, "GPS编号不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtGpsNumber);
            return;
        }

        String truckNumber = mEtTruckNumber.getText().toString().trim();
        if (TextUtils.isEmpty(truckNumber)) {
            Toast.makeText(mContext, "车牌号码不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtTruckNumber);
            return;
        }

        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtPhone);
            return;
        }else if (!TextUtil.isMobileNO(phone)){
            Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtPhone);
            return;
        }

        String capacity = mEtLoadCapacity.getText().toString().trim();
        if (TextUtils.isEmpty(capacity)) {
            Toast.makeText(mContext, "载重量不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtLoadCapacity);
            return;
        }
        String productionTime = mTvProductionTime.getText().toString().trim();
        /*if (TextUtils.isEmpty(productionTime)) {
            Toast.makeText(mContext, "生产日期不能为空", Toast.LENGTH_SHORT).show();
            return;
        }*/

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("type", "1");
        mapParams.put("companyId", companyId);
        mapParams.put("deviceId", mEtDeviceNumber.getText().toString());
        mapParams.put("gpsId", mEtGpsNumber.getText().toString());
        mapParams.put("carNumber", mEtTruckNumber.getText().toString());
        mapParams.put("phone", mEtPhone.getText().toString());
        mapParams.put("loadCapacity", mEtLoadCapacity.getText().toString());
        mapParams.put("mfgDate", mTvProductionTime.getText().toString());
        truckAddTask=TruckAddTask.getInstance(mContext,mHandler);
        truckAddTask.updateTruckInfo(mapParams);
    }



}
