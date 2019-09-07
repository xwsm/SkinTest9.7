package com.mm.baseview.base.skin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mm.baseview.base.IActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/6/19 15:29
 * @description:
 */
public abstract class SkinBaseActivity extends AppCompatActivity implements IActivity {
    SkinFactory skinFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().setContext(this);
        skinFactory = new SkinFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), skinFactory);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        getPermission();

    }
    public void getPermission() {
        String[] array = getUsesPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(array, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PERMISSION_GRANTED && requestCode == 100) {
                //同意了权限
            } else {
                //拒绝了权限
            }
        }
    }


    /**
     * 获取manifests配置的权限
     *
     * @return
     */
    private String[] getUsesPermission() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] usesPermissionsArray = packageInfo.requestedPermissions;
            return usesPermissionsArray;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    public void changeSkin() {
        skinFactory.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        skinFactory.apply();
    }
}
