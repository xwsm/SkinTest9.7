package com.mm.baseview.base.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/6/19 15:32
 * @description:
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    /**
     * 装载需要换肤的容器
     */
    List<SkinView> viewList = new ArrayList<>();
    private String[] prxfixList = {"android.widget", "android.view", "android.webkit"};

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        /*Log.i("SkinFactory", name);for (int i=0;i<attrs.getAttributeCount();i++){Log.i("SkinFactoryName:",attrs.getAttributeName(i));}*/
        View view = null;
        //区分是不是自定义的控件或者特殊控件 自定义控件包含包名
        if (name.contains(".")) {
            view = onCreateView(name, context, attrs);
        } else {
            for (String s : prxfixList) {
                String packageName = s + "." + name;
                view = onCreateView(packageName, context, attrs);
                if (view != null) {
                    break;
                }
            }
        }

        if (view != null) {
            //如果控件不为空，就去判断这个控件是否需要换肤 如果需要就给他收集起来
            parseView(view, name, attrs);
        }

        return view;
    }

    /**
     * 如果控件不为空，就去判断这个控件是否需要换肤 如果需要就给他收集起来
     *
     * @param view
     * @param name
     * @param attrs
     */
    private void parseView(View view, String name, AttributeSet attrs) {
        List<SkinItem> itemList = new ArrayList<>();
        //遍历这个控件所有属性的集合
        for (int x = 0; x < attrs.getAttributeCount(); x++) {
            //属性的名字
            String attributeName = attrs.getAttributeName(x);
            //获取属性的资源ID
            String attributeValue = attrs.getAttributeValue(x);
            //判断这条属性是不是带有background textColor color src

            //@0是代码中  @null    应该写一套正规的规则，而不是 预坑填坑
            //# 类型是直接设置的颜色值
            boolean isContains = (attributeName.contains("background") || attributeName.contains("src") ||
                    attributeName.contains("textColor") || attributeName.contains("color")) && !attributeValue.contains("#") && !attributeValue.equals("@0");

            if (isContains) {
                //如果符合条件，就证明这条属性是需要进行换肤的
                //获取到资源ID
                Log.i("Tagssss:", attributeValue + "-->Contains:" + !attributeValue.contains("#"));
                int resId = Integer.parseInt(attributeValue.substring(1));
                //获取到属性的值的类型
                String typeName = view.getResources().getResourceTypeName(resId);
                //获取到属性的值的名字 colorPirmry
                String entryName = view.getResources().getResourceTypeName(resId);
                SkinItem skinItem = new SkinItem(attributeName, resId, entryName, typeName);
                Log.i("Tags:", attributeValue.substring(1) + "---" + typeName + "---" + entryName + "---");
                itemList.add(skinItem);
            }
        }
        //如果条件为true 说明控件需要换肤
        if (itemList.size() > 0) {
            //说明控件需要换肤
            SkinView skinView = new SkinView(view, itemList);
            viewList.add(skinView);
            skinView.apply();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            //获取到控件的类对象
            Class aClass = context.getClassLoader().loadClass(name);
            //获取构造方法 Context.class,AttributeSet.class
            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
            //实例化控件
            view = constructor.newInstance(context, attrs);
        } catch (Exception e) {
            /*e.printStackTrace();//禁止输出,在控件的实例化中会出现错误*/
        }
        return view;
    }

    /**
     * 封转控件
     */
    class SkinView {
        View view;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        /**
         * 需要换肤操作的属性对象
         */
        List<SkinItem> list;

        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getName().equals("background")) {
                    if (skinItem.getTypeName().equals("color")) {
                        try {
                            view.setBackgroundResource(0);
                            view.setBackgroundDrawable(null);
                            view.setBackground(null);
                        } catch (Exception e) {

                        }
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (skinItem.getTypeName().equals("drawable")
                            || skinItem.getTypeName().equals("mipmap")) {
                        try {
                            view.setBackgroundResource(0);
                            view.setBackgroundDrawable(null);
                            view.setBackground(null);
                        } catch (Exception e) {
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        } else {
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                } else if (skinItem.getName().equals("src")) {
                    try {
                        view.setBackgroundResource(0);
                        view.setBackgroundDrawable(null);
                        view.setBackground(null);
                    } catch (Exception e) {
                    }
                    if (view instanceof ImageView) {
                        ((ImageView) view).setImageDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                    }
                } else if (skinItem.getName().equals("textColor")) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (view instanceof Button) {
                        ((Button) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (view instanceof EditText) {
                        ((EditText) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }

                }
            }
        }
    }



    public void apply() {
        for (SkinView skinView : viewList) {
            skinView.apply();
        }
    }

    /**
     * 封转每一条属性
     */
    class SkinItem {
        /**
         * 属性名字 background textColor text src
         */
        String name;

        public SkinItem(String name, int resId, String entryName, String typeName) {
            this.name = name;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }


        /**
         * 属性的ID
         */
        int resId;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }


        public String getEntryName() {
            return entryName;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        /**
         * 属性的值的名字 colorPrimary
         */
        String entryName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * 属性的值的类型 COLOR drawable
         */
        String typeName;

    }


}
