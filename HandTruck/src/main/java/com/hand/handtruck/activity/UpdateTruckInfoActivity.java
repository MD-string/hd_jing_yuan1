package com.hand.handtruck.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.TextUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CompanyResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.CompanyListTask;
import com.hand.handtruck.domain.TruckAddTask;
import com.hand.handtruck.model.CompanyBean;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.utils.LogUtil;
import com.parkingwang.vehiclekeyboard.support.PopupKeyboard;
import com.parkingwang.vehiclekeyboard.view.InputView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//修改页面
public class UpdateTruckInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlTitle;
    private TextView mEtSelectCompany;
    private ImageView mIvCompanyArrow;
    private EditText mEtDeviceNumber;
    private EditText mEtGpsNumber;
    private TextView mTvOkTruckN,mTvCancelTruckN,mEtTruckNumber;
    private EditText mEtPhone;
    private EditText mEtLoadCapacity;
    private TextView mEtProductionTime;
    private Button mBtnAddTruck;
    private CompanyTruckBean truckModel;
    private Activity mContext;
    private TimePickerView pvCustomTime;
    private String companyId="";
    private InputView mInputView;
    private PopupKeyboard mPopupKeyboard;
    private LinearLayout dialog_truckN_input;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
              //获取公司
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    LogUtil.e("修改车俩获取公司列表成功");
                    CompanyResultBean companyResult = (CompanyResultBean) msg.obj;
                    List<CompanyBean> companyList = companyResult.getResult();
                    for (int i = 0;i<companyList.size();i++){
                        if (truckModel.getCompanyName().equals(companyList.get(i).getCompanyName())){
                            companyId=companyList.get(i).getId();
                        }
                    }

                    break;
                //失败
                case ConstantsCode.MSG_REQUEST_FAIL1:

                    break;
                  /*修改车辆信息成功*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:

                    break;
                //失败
                case ConstantsCode.MSG_REQUEST_FAIL:

                    break;

            }
        }
    };
    private String token;
    private TruckAddTask truckAddTask;
    private CompanyListTask companyTruckTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_truck_info;
    }

    @Override
    protected void findViews() {
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);


        mEtSelectCompany = (TextView) findViewById(R.id.et_select_company);
        mIvCompanyArrow = (ImageView) findViewById(R.id.iv_company_arrow);
        mEtDeviceNumber = (EditText) findViewById(R.id.et_device_number);
        mEtGpsNumber = (EditText) findViewById(R.id.et_gps_number);
        mEtTruckNumber = (TextView) findViewById(R.id.tv_truck_number);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtLoadCapacity = (EditText) findViewById(R.id.et_load_capacity);
        mEtProductionTime = (TextView) findViewById(R.id.et_production_time);
        dialog_truckN_input = (LinearLayout)findViewById(R.id.dialog_truckN_input_u);
        mBtnAddTruck = (Button) findViewById(R.id.btn_add_truck);
        mInputView = (InputView)findViewById(R.id.input_view);
        mTvOkTruckN = (TextView)findViewById(R.id.tv_okTruckN);
        mTvCancelTruckN = (TextView) findViewById(R.id.tv_cancelTruckN);

        mTvTitle.setText("车载监控平台");
        mIvCompanyArrow.setVisibility(View.GONE);
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, this);

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
        mTvBack.setOnClickListener(this);
        mEtProductionTime.setOnClickListener(this);
        mBtnAddTruck.setOnClickListener(this);
        mEtTruckNumber.setOnClickListener(this);
        mTvOkTruckN.setOnClickListener(this);
        mTvCancelTruckN.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        mContext = UpdateTruckInfoActivity.this;
        initCustomTimePicker();
        Bundle bundle = getIntent().getExtras();
        truckModel = (CompanyTruckBean) bundle.getSerializable("truckModel");
        SharedPreferences sp = getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        mEtSelectCompany.setText(truckModel.getCompanyName());
        mEtDeviceNumber.setText(truckModel.getDeviceId());
        mEtGpsNumber.setText(truckModel.getGpsId());
        mEtTruckNumber.setText(truckModel.getCarNumber());
        mEtLoadCapacity.setText(truckModel.getLoadCapacity());
        mEtProductionTime.setText(truckModel.getMfgDate());
        companyTruckTask = new CompanyListTask(mContext, mHandler);
        initCompanyList();

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
            //返回
            case R.id.tv_back:
                finish();
                break;

            //选择生产日期
            case R.id.et_production_time:
                hideKeyBorad();
                pvCustomTime.show();
                break;
            //车牌号码
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
                mInputView.updateNumber("");
                mInputView.performFirstItem();
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(mContext);
                }

                break;
            //修改数据
            case R.id.btn_add_truck:
                hideKeyBorad();
                if (!CommonKitUtil.isNetworkAvailable(this)) {
                    ToastUtil.showMsgShort(this, ConstantsCode.NETWORK_ERROR);
                    return;
                }
                if (TextUtils.isEmpty(mEtDeviceNumber.getText())) {
                    ToastUtil.showMsgShort(mContext, "设备编号不能为空");
                    return;
                } else if (TextUtils.isEmpty(mEtGpsNumber.getText())) {
                    ToastUtil.showMsgShort(mContext, "GPS编号不能为空");
                    return;
                } else if (TextUtils.isEmpty(mEtTruckNumber.getText())) {
                    ToastUtil.showMsgShort(mContext, "车牌号码不能为空");
                    return;
                }
                String phone = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!TextUtil.isMobileNO(phone)){
                    Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(mEtLoadCapacity.getText())) {
                    ToastUtil.showMsgShort(mContext, "载重量不能为空");
                    return;
                } else if (TextUtils.isEmpty(mEtProductionTime.getText())) {
                    ToastUtil.showMsgShort(mContext, "生产日期不能为空");
                    return;
                }


                Map<String, String> mapParams = new HashMap<>();
                mapParams.put("token", token);
                mapParams.put("type", "2");
                mapParams.put("companyId", companyId);
                mapParams.put("deviceId", mEtDeviceNumber.getText().toString());
                mapParams.put("gpsId", mEtGpsNumber.getText().toString());
                mapParams.put("carNumber", mEtTruckNumber.getText().toString());
                mapParams.put("phone", mEtPhone.getText().toString());
                mapParams.put("loadCapacity", mEtLoadCapacity.getText().toString());
                mapParams.put("mfgDate", mEtProductionTime.getText().toString());
                truckAddTask=TruckAddTask.getInstance(this,mHandler);
                truckAddTask.updateTruckInfo(mapParams);
                break;
        }
    }
    private void hideKeyBorad() {
        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
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
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mEtProductionTime.setText(getTime(date));
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
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.6f)
                .setTextColorCenter(0xFF0E82EB)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
}
