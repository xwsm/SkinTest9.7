package com.skintest97;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.mm.baseview.base.skin.SkinBaseActivity;
import com.mm.baseview.base.skin.SkinManager;


/**
 * @author hkc
 */
public class MainActivity extends SkinBaseActivity {

    @Override
    public void initView() {


    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
                loadData1(null);


        }
    };
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            loadData2(null);


            //loadData2(null);


        }
    };
    Handler handler3 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            loadData3(null);


            //loadData2(null);


        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {


        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void loadData1(View view) {
        Log.d("XXX", Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin1.apk");
        changeSkinPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin1.apk");
    }

    public void loadData2(View view) {
        Log.d("XXX", Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin2.apk");
        changeSkinPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin2.apk");
    }

    public void loadData3(View view) {
        Log.d("XXX", Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin3.apk");
        changeSkinPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/skin/skin3.apk");
    }

    public void changeSkinPath(String path) {
        SkinManager.getInstance().loadSkinApk(path);
        changeSkin();
    }

    public void loadData4(View view) {
        startActivity(new Intent(this, AA.class));
    }
}
