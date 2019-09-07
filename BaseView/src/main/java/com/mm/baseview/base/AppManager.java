package com.mm.baseview.base;

/**
 * Created by System on 2018/6/22.
 */

import android.app.Activity;
import android.content.Context;

import java.util.ListIterator;
import java.util.Stack;

/**
 * Activity管理类
 */
public class AppManager {

    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack != null &&  activityStack.size()>0) {
            Activity activity = activityStack.lastElement();
            return activity;
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishWithActivity() {

    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager.restartPackage(context.getPackageName());
            int nPid = android.os.Process.myPid();
            android.os.Process.killProcess(nPid);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束非cls 的所有activity
     *
     * @param cls
     */
    public void finishNotActivity(Class<?> cls) {
        ListIterator<Activity> iterator = activityStack.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束非cls 的所有activity
     *
     * @param cls
     */
    public void finishNotActivity(String cls) {
        ListIterator<Activity> iterator = activityStack.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().getName().contains(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }
}