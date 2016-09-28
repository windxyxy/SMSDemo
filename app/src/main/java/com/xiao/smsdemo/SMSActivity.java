package com.xiao.smsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/*
* 短信验证码：
* 一、在Mod.com注册，下载短信验证SDK
* 二、在mod.com新建应用，记住APPKEY和APPSECRET码
* 三、新建自己的Demo，将下载的SDK包中的SMSSDK中的4个包放到自己Moudle下的libs目录中
* 四、在Moudle的build.gradle中添加如下代码：
*                       repositories{
*                          flatDir{
*                              dirs 'libs' //就是你放aar的目录地址
*                                  }
*                                }
*     然后添加如下依赖：     compile name:'SMSSDK-2.1.1',ext:'aar'
*                           compile name:'SMSSDKGUI-2.1.1',ext:'aar'
*
* 五、在清单文件（Mainfest.xml）中，添加相关权限，并注册一个固定的Activity,（具体查看mainfest.xml）
* 六、初始化SMSSDK（调用SMSSDK中的initSDK方法）
* 七、点击进入注册页面，具体步骤参照下面代码
*
* */

/**
* Created by Monster
* on 2016/8/31.
*/
public class SMSActivity extends AppCompatActivity {
    private Button btn_bindsdk;
    private static String APPKEY = "16a2cf089b86b";
    private static String APPSECRET = "4921c2aabf9938bd438292bad5bc4792";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        //初始化SDK
        SMSSDK.initSDK(this,APPKEY,APPSECRET,false);

        btn_bindsdk = (Button) findViewById(R.id.btn_bindsdk);
        //点击注册
        btn_bindsdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入注册页面
                RegisterPage registerPage = new RegisterPage();
                //注册回调事件
                registerPage.setRegisterCallback(new EventHandler(){
                    //事件完成后调用
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        //判断结果是否已经完成（RESULT_COMPLETE = -1;也可以写result == -1）
                        if(result == SMSSDK.RESULT_COMPLETE){
                            //获取数据data（因为请求事件是以键值对的形式请求，所以需要将data强制转换为HashMap对象）
                            HashMap<String,Object> maps = (HashMap<String, Object>) data;
                            //国家
                            String country = (String) maps.get("country");
                            //电话
                            String phone = (String) maps.get("phone");
                            submitUserInfo(country,phone);
                        }
                    }
                });
                //在此显示注册页面
                registerPage.show(SMSActivity.this);
            }
        });
    }
    //提交用户信息
    public void submitUserInfo(String country , String phone){
        Random random = new Random();
        String uid = Math.abs(random.nextInt())+"";
        String nickname = "昵称";
        //submitUserInfo(java.lang.String uid,String nickname,String avatar,String country,String phone)
        //有5个参数，分别是userID，昵称，，，国家，电话
        SMSSDK.submitUserInfo(uid,nickname,null,country,phone);
    }

}
