package com.mm.baseview.base.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @date: 2019/6/19 16:10
 * @description: 动态加载技术，用来加载第三方插件Apk的资源对象
 */
public class SkinManager {

    private static SkinManager skinManager = new SkinManager();

    private SkinManager() {

    }

    public static SkinManager getInstance() {
        return skinManager;
    }

    /**
     * 第三方插件皮肤APK中的资源对象
     */
    private Resources resources;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 插件APK包名
     */
    private String skinPackageName;

    public void setContext(Context context) {
        this.context = context;
    }

    AssetManager assetManager;

    /**
     * 获取插件APK中的资源对象方法
     */
    public void loadSkinApk(String path) {
        try {
            //获取到包管理器
            PackageManager packageManager = context.getPackageManager();
            //通过包管理器,获取第三方插件apk的包名
            skinPackageName = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
            try {
                closeResource();
                assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, path);
                resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("XXX", e.getLocalizedMessage());
            Toast.makeText(context, "皮肤资源加载失败", Toast.LENGTH_SHORT).show();

        }

    }

    void closeResource() {
        if (assetManager != null) {
            assetManager.close();
            assetManager=null;
        }
        if (resources != null) {
            resources = null;
        }
    }

    /**
     * 根据传入的资源id，获取插件APK中的资源对象的名字类型和类型都一样的资源的id
     *
     * @return
     */
    public int getColor(int id) {
        if (resources == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getColor(id);
            } else {
                return ContextCompat.getColor(context, id);
            }

        }
        //获取到属性的值的名字
        String entryName = context.getResources().getResourceEntryName(id);
        //获取到属性值的类型
        String typeName = context.getResources().getResourceTypeName(id);
        //获取到的就是名字跟类型都一样的第三方插件APK的资源ID
        int identifier = resources.getIdentifier(entryName, typeName, skinPackageName);
        if (identifier == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getColor(id);
            } else {
                return ContextCompat.getColor(context, id);
            }
//            return id;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(identifier, context.getTheme());
        } else {
            return resources.getColor(identifier);
        }

    }

    public Drawable getDrawable(int id) {
        if (resources == null) {
            return ContextCompat.getDrawable(context, id);
        }
        //获取到属性的值的名字
        String entryName = context.getResources().getResourceEntryName(id);
        //获取到属性值的类型
        String typeName = context.getResources().getResourceTypeName(id);
        //获取到的就是名字跟类型都一样的第三方插件APK的资源ID
        int identifier = resources.getIdentifier(entryName, typeName, skinPackageName);
        if (identifier == 0) {
            return ContextCompat.getDrawable(context, id);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(identifier,context.getTheme());
        } else {
            return resources.getDrawable(identifier);
        }
    }

}
