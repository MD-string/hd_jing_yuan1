package com.hand.handtruck.ui.Examine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.BottomSelectPopupWindow;
import com.hand.handtruck.Widget.OnMyItemClickListener;
import com.hand.handtruck.application.MyApplication;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.Examine.presenter.InformationBasicPresenter;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ACache;
import com.hand.handtruck.utils.ImageUtils;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;

import androidx.annotation.RequiresApi;


//取证处理
public class ReaAndColFrgment extends BaseFragment implements View.OnClickListener,IInformationBasicView{

    private static final String TAG = "WeightFragment";
    private Context context;
    private BroadcastReceiver receiver;
    private LinearLayout ll_back;
    private ImageView img_add;
    private EditText et_remark;
    private Button bt_ok;
    private RelativeLayout rl_all;
    private BottomSelectPopupWindow pickPhotoDlg;
    private File cameraFile;
    private InformationBasicPresenter mpresenter;
    private String mpath;
    private String token;
    private Button bt_save;
    private boolean isSave;
    private ACache acache;
    private SharedPreferences sp;
    private FormBean bean;
    private String orderId,statusId;
    private TextView mTvDetailTruckNumber,mTvDetailDeviceNumber,mFormNumber,mStuff,mTvDetailWeight,mTvDetailWeightTime,mTranpTime,mUnloadEndTime;
    private DecimalFormat mineformat= new DecimalFormat("0.00");
    private Button bt_close;
    private boolean isNetImg;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        View view = inflater.inflate(R.layout.activity_take_photo, container, false);

        sp = context.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        bean= (FormBean)getArguments().getSerializable("formBean");

        orderId=bean.getOrderCode();
        statusId=bean.getId();
        initView(view);
        initPhotoError();
        mpresenter = InformationBasicPresenter.getInstance(context, handler);
        return view;
    }

    private void initView(View view) {
        rl_all=(RelativeLayout)view.findViewById(R.id.rl_all);
        ll_back=(LinearLayout)view.findViewById(R.id.ll_back);
        ll_back.setVisibility(View.GONE);
        img_add=(ImageView)view.findViewById(R.id.img_add);
        et_remark=(EditText)view.findViewById(R.id.et_remark);
        bt_ok=(Button)view.findViewById(R.id.bt_ok);
        bt_save=(Button)view.findViewById(R.id.bt_save);

        img_add.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        bt_save.setOnClickListener(this);

        bt_close=(Button)view.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(this);

        mTvDetailTruckNumber = (TextView) view.findViewById(R.id.tv_detail_truck_number); //车牌号

        mTvDetailDeviceNumber = (TextView) view.findViewById(R.id.tv_detail_bussiner);  //经销商
        mFormNumber=(TextView)view.findViewById(R.id.tv_form_number);         //发货单号
        mStuff=(TextView)view.findViewById(R.id.tv_stuff);//货物
        mTvDetailWeight = (TextView) view.findViewById(R.id.tv_stuff_type);  //货物类型
        mTvDetailWeightTime = (TextView) view.findViewById(R.id.tv_weight_purse); //净重

        mTranpTime = (TextView) view.findViewById(R.id.tv_start_car_date);//发车时间
        mUnloadEndTime=(TextView)view.findViewById(R.id.tv_unload_end_time);//卸货完成时间


        String carNumber=bean.getCarNumber();
        mTvDetailTruckNumber.setText(carNumber);
        mTvDetailDeviceNumber.setText(bean.getCustName());
        mFormNumber.setText(bean.getOrderCode());
        mStuff.setText(bean.getProdName());
        mTvDetailWeight.setText(bean.getPackType());
        String weight=bean.getSendWeight();
        String str=mineformat.format(Float.parseFloat(weight));
        mTvDetailWeightTime.setText(str+"吨");


        String leaveDate=bean.getLeaveTime();
        mTranpTime.setText(leaveDate);

        String endTime=bean.getUnloadEndTime();
        mUnloadEndTime.setText(endTime);

        String detailMsg=bean.getDetailMsg();
        if(Tools.isEmpty(detailMsg)){
            et_remark.setText("");
        }else{
            et_remark.setText(detailMsg);
            et_remark.setSelection(detailMsg.length()-1);
        }

        try{
            String imgUrl=bean.getCheckImgUrl();//   /uploadfile/order/20012020032540000133.jpg
            if(!Tools.isEmpty(imgUrl)){
                String url=Constants.GET_URL();
                String imgUrl1=  url.replace("/api/V1","")+imgUrl;
                mpath=imgUrl1;
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().displayImage(imgUrl1,img_add);
                isNetImg=true;
            }
        }catch (Exception e){
            e.printStackTrace();
            mpath="";
            isNetImg=false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add:
                checkPermission();
                break;
            case R.id.bt_ok://审核
//                if(Tools.isEmpty(mpath)){
//                    showTips("请先拍照");
//                    return;
//                }
                //                String  status=bean.getOrderStatus().getDetailStatus(); ////人工处理状态，0未处理、1已处理
                //                if(!"1".equals(status)){
                //                    showTips("没有取证处理");
                //                    return;
                //                }
                String msg=et_remark.getText().toString().trim();
                if(Tools.isEmpty(msg)){
                    showTips("处理意见不能为空");
                    return;
                }
                isSave=false;
                String flag="1";
                if(isNetImg){
                    mpresenter.getTransportListNoImg(token,orderId,mpath,statusId,msg,flag);
                }else{
                    if(!Tools.isEmpty(mpath)){

                        mpresenter.getTransportList(token,orderId,mpath,statusId,msg,flag);
                    }else{
                        mpresenter.getTransportListNoImg(token,orderId,mpath,statusId,msg,flag);
                    }
                }

                break;
            case R.id.bt_save://保存
//                if(Tools.isEmpty(mpath)){
//                    showTips("请先拍照");
//                    return;
//                }
                isSave=true;
                String msg1=et_remark.getText().toString().trim();
                if(Tools.isEmpty(msg1)){
                    showTips("处理意见不能为空");
                    return;
                }
                String flag1="0";
                if(isNetImg){
                    mpresenter.getTransportListNoImg(token,orderId,mpath,statusId,msg1,flag1);
                }else{
                    if(!Tools.isEmpty(mpath)){
                        mpresenter.getTransportList(token,orderId,mpath,statusId,msg1,flag1);
                    }else{
                        mpresenter.getTransportListNoImg(token,orderId,mpath,statusId,msg1,flag1);
                    }
                }
            case R.id.bt_close://关闭
                getActivity().finish();
                break;
        }

    }

    /**
     * 弹出选择图片方式询问
     */
    public void photoDialogPop() {
        if (pickPhotoDlg == null) {
            pickPhotoDlg = new BottomSelectPopupWindow(context, new String[] { "拍照", "从相册选取" }, null);
            pickPhotoDlg.setOnItemClickListener(new OnMyItemClickListener() {

                @Override
                public void onItemClick(View parent, View view, int which) {
                    if (which == 0) {
                        pickToCamera();
                    } else {
                        pickToAlbum();
                    }
                }
            });
        }
        pickPhotoDlg.showAtLocation(rl_all, Gravity.BOTTOM, 0, 0);
    }

    private void pickToAlbum() {
        /*打开相册*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }
    private void pickToCamera() {
        if (!checkCameraPermission()) {
            showTips("摄像头权限未打开，请打开后再试");
            return;
        }
        String path= MyApplication.context.getCacheDir().getPath() +"/hand/" + "hd_img" + "/";
        File mZipFile = new File(path);
        if (!mZipFile.exists()) {
            mZipFile.mkdirs();
        }
        cameraFile = new File(path, System.currentTimeMillis() + ".jpg");


        Uri imageUri = FileProvider.getUriForFile(context,context.getResources().getString(R.string.APP_File_Provider),cameraFile); //存储位置


        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, imageUri), ConstantsCode.REQUEST_CODE_CAMERA);
    }


    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }


    private boolean checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{

            return true;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsCode.REQUEST_CODE_CAMERA) {// 返回拍照结果
            if (cameraFile != null) {
                String picPath=cameraFile.getAbsolutePath();
                Message msg=new Message();
                msg.what=1;
                msg.obj=picPath;
                handler.sendMessage(msg);
            }
        } else if (requestCode == 1000 ) {
            String path = ImageUtils.getImageAbsolutePath(context, data.getData());
            Message msg1=new Message();
            msg1.what=1;
            msg1.obj=path;
            handler.sendMessage(msg1);

        }
    }
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .CAMERA)) {
                //                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);

        } else {//已经授权

            photoDialogPop();

            Log.e(TAG,"TAG_SERVICE"+ "checkPermission: 已经授权！");
        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg)  {
           try{
//               if (getActivity().isFinishing()) {
//                   return;
//               }
               switch (msg.what) {
                   case 1://
                       mpath=(String)msg.obj;
                       Bitmap map=getBitmap(mpath,100,100);
                       if(map !=null){
                           img_add.setImageBitmap(map);
                       }
                       isNetImg=false;
                       break;
                   case ConstantsCode.MSG_REQUEST_SUCCESS:
                       if(isSave){
                           ToastUtil.getInstance().showCenterMessage(MyApplication.context, "保存成功");
                       }else{
                           ToastUtil.getInstance().showCenterMessage(MyApplication.context, "审核成功");
                       }
                       isSave=false;

                       Intent readIntent=new Intent(ConstantsCode.DISCOVER_GET_EXAMINE);//刷新页面
                       context.sendBroadcast(readIntent);

                       getActivity().finish();
                       break;
                   case ConstantsCode.MSG_REQUEST_FAIL:
                       if(isSave){
                           ToastUtil.getInstance().showCenterMessage(MyApplication.context, "保存失败");
                       }else{
                           ToastUtil.getInstance().showCenterMessage(MyApplication.context, "审核失败");
                       }
                       isSave=false;
                       break;
                   default:
                       break;
               }
           }catch (Exception e){
               e.printStackTrace();
           }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * 根据路径获取图片
     *
     * @param filePath  文件路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大高度
     * @return bitmap
     */
    private static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * Return the sample size.
     *
     * @param options   The options.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the sample size
     */
    private static int calculateInSampleSize(final BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }


    // android 7.0系统解决拍照的问题
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initPhotoError(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void loadSuccess(String msg) {
        showTips(msg);
    }

    @Override
    public void loadFail(String msg) {
        showTips(msg);
    }

    //系统方法,从requestPermissions()方法回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //确保是我们的请求
        if (requestCode == 110) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "权限被授予", Toast.LENGTH_SHORT).show();

                photoDialogPop();

            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
