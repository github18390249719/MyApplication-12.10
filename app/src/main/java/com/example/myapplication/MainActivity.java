package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * changed in2017.9.6
 */
public class MainActivity extends CheckPermissionsActivity implements View.OnClickListener{

    //UI Object
    private TextView txt_topbar;
    protected TextView txt_channel;
    protected TextView txt_message;
    protected TextView txt_better;
    protected TextView txt_setting;
    private FrameLayout ly_content;

    //Fragment Object
    private Fragment_homepage fg1;
    private PerimeterFragment fg2;
    private MyFragment fg3;
    protected Fragment_unlogin fg4;
    protected Fragment_mine fg5;
    private FragmentManager fManager;

    protected boolean state;

    //确定是否退出程序的标识变量
    private static boolean isExit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fManager = getFragmentManager();
        state=getSharedPreferences("data",MODE_PRIVATE).getBoolean("state",false);
        bindViews();
        txt_channel.performClick();   //模拟一次点击，既进去后选择第一项
    }


    //对登录界面的返回结果进行处理
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            state=true;
            setSelected();
            txt_setting.setSelected(true);
            fg5 = new Fragment_mine();
            fManager.beginTransaction().add(R.id.ly_content, fg5).commit();
        }
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        txt_channel = (TextView) findViewById(R.id.txt_channel);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_better = (TextView) findViewById(R.id.txt_better);
        txt_setting = (TextView) findViewById(R.id.txt_setting);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);

        //为底部控件设置监听器
        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_better.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    protected void setSelected(){
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
        txt_better.setSelected(false);
        txt_setting.setSelected(false);
    }

    //隐藏所有Fragment
    protected void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
        if(fg5 != null)fragmentTransaction.hide(fg5);
    }

    //模拟一次点击
    public void setFragment(){
        txt_setting.performClick();
    }

    //通过handler修改标识变量
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //通过两次点击返回键后标识变量的状态确定是否退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息，等待时间为2秒
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);}

    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        setSelected();
        switch (v.getId()) {
            case R.id.txt_channel:
                txt_channel.setSelected(true);
                if (fg1 == null) {
                    fg1 = new Fragment_homepage();
                    fTransaction.add(R.id.ly_content, fg1);
                } else {
                    fTransaction.show(fg1);
                }
                break;
            case R.id.txt_message:
                txt_message.setSelected(true);
                if (fg2 == null) {
                    fg2 = new PerimeterFragment();
                    fTransaction.add(R.id.ly_content, fg2);
                } else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.txt_better:
                txt_better.setSelected(true);
                if (fg3 == null) {
                    fg3 = new MyFragment("第三个Fragment");
                    fTransaction.add(R.id.ly_content, fg3);
                } else {
                    fTransaction.show(fg3);
                }
                break;
            case R.id.txt_setting:
                txt_setting.setSelected(true);
                if (state) {
                    if (fg5 == null) {
                        fg5 = new Fragment_mine();
                        fTransaction.add(R.id.ly_content, fg5);
                    } else
                        fTransaction.show(fg5);
                } else {
                    if (fg4 == null) {
                        fg4 = new Fragment_unlogin();
                        fTransaction.add(R.id.ly_content, fg4);
                    } else
                        fTransaction.show(fg4);
                }
                break;
        }
            fTransaction.commit();
        }
}
