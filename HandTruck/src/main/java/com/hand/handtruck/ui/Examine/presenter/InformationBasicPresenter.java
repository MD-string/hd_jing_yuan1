package com.hand.handtruck.ui.Examine.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.utils.FilePathUtils;
import com.hand.handtruck.utils.ImageUtil;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/*
 * 动态  数据处理
 */
public class InformationBasicPresenter {

    private static Handler mHandler;
    private static Context mContext;
    private static InformationBasicPresenter instance = null;
    public InformationBasicPresenter() {
    }


    public static InformationBasicPresenter getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new InformationBasicPresenter();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*运输任务列表接口*/
    public void getTransportList(String token,String orderId,String path,String orderStatusId,String msg,String flag) {
        try{
            Bitmap bitmap = ImageUtil.getImage(path,480,800); //此时返回 bm 为空
            // 计算图片缩放比例

            // 首先保存图片
            String dirPath = FilePathUtils.getRecvFilePath();
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            final File file = new File(dirPath,fileName);
            if(!file.exists()){
                file.createNewFile();
            }

            //            copyFile(path,file.getAbsolutePath());

            saveBitmap(bitmap,dirPath,fileName);

            Map<String, String> map = new HashMap<String, String>();
            map.put("file", file.getAbsolutePath());
            map.put("token", token);
            map.put("orderCode", orderId);
            map.put("id", orderStatusId);
            map.put("detailMsg", msg);
            map.put("flag", flag);

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Disposition", "form-data;filename=enctype");
            File file1 = new File(file.getAbsolutePath());
            if (!file1.exists()) {
                LogUtil.e("文件不存在，请修改文件路径");
                return;
            }
            String url = Constants.HttpUrl.CHECK_ORDER;
            LogUtil.e("获取运输任务列表URL==" + url + map.toString());
            OkHttpUtils.post().url(url).params(map)
                    .headers(headers)
                    .addFile("file",file.getName(),file)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                    ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject jsonObject = null;
                        String mMessage="";
                        try {
                            jsonObject = new JSONObject(response.toString());
//                            String code = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            mMessage=msg;
                            if ( "success".equals(msg) || msg.contains("成功")) {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_SUCCESS);
                            } else {
                                //                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                                doLoginAgain(mMessage);//重新登录
                            }
                        } catch (Exception e) {
                            doLoginAgain(mMessage);//重新登录
                            e.printStackTrace();
                        }

                    } else {
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /*运输任务列表接口*/
    public void getTransportListNoImg(String token,String orderId,String path,String orderStatusId,String msg,String flag) {
        try{

            Map<String, String> map = new HashMap<String, String>();
            map.put("token", token);
            map.put("orderCode", orderId);
            map.put("id", orderStatusId);
            map.put("detailMsg", msg);
            map.put("flag", flag);

            String url = Constants.HttpUrl.CHECK_ORDER;
            LogUtil.e("获取运输任务列表URL==" + url + map.toString());
            OkHttpUtils.post().url(url).params(map)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                    ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject jsonObject = null;
                        String mMessage="";
                        try {
                            jsonObject = new JSONObject(response.toString());
                            //                            String code = jsonObject.getString("result");
                            String msg = jsonObject.getString("msg");
                            mMessage=msg;
                            if ( "success".equals(msg) || msg.contains("成功")) {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_SUCCESS);
                            } else {
                                //                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                                doLoginAgain(mMessage);//重新登录
                            }
                        } catch (Exception e) {
                            doLoginAgain(mMessage);//重新登录
                            e.printStackTrace();
                        }

                    } else {
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            long size=oldfile.length();
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                oldfile.delete();//删除原文件
            }
            DLog.d("infor","复制图片成功");
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

   // android中保存Bitmap图片到指定文件夹中的方法。

    /** 保存方法 */
    public void saveBitmap(Bitmap bm, String path, String name) {
        File f = new File(path, name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void doLoginAgain(String message){
        if(message.contains("重新登录")){
            Intent i=new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i);
        }
    }
}
