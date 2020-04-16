package com.hand.handlibray.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.handlibray.R;


/**
 * Created by kk on 2016/7/30.
 */
public class PromptDialog extends Dialog implements View.OnClickListener {

    private Context mContext;//上下文对象
    private TextView btn_I_know;
    private TextView btn_cancel;
    private ImageView iv_close,iv_tips;
    private TextView tv_content;
    private String mcontent="";
    private String mtitle="我知道了";
    private int types;
    public PromptDialog(Context context, String content) {
        super(context);
        this.mcontent=content;
        init(context);
    }

    public PromptDialog(Context context, String content, String title, int type) {
        super(context);
        this.mcontent=content;
        this.mtitle=title;
        this.types=type;
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        btn_I_know= (TextView) view.findViewById(R.id.btn_I_know);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        btn_cancel= (TextView) findViewById(R.id.btn_cancel);
        iv_close= (ImageView) findViewById(R.id.iv_close);
        iv_tips= (ImageView) findViewById(R.id.iv_tips);
        if(types==2){
            btn_cancel.setVisibility(View.VISIBLE);
            //iv_tips.setImageResource(R.mipmap.tips_error);
        }
        tv_content.setText(mcontent);
        btn_I_know.setText(mtitle);
        btn_I_know.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
      int id=view.getId();
        if (id == R.id.btn_I_know) {
            this.dismiss();

        } else if (id == R.id.btn_cancel) {
            this.dismiss();

        } else if (id == R.id.iv_close) {
            this.dismiss();

        }
    }
}
