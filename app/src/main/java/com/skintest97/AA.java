package com.skintest97;

import android.content.Intent;
import android.view.View;

import com.mm.baseview.base.skin.SkinBaseActivity;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: hkc #3
 * @date: 2019/9/7 8:59
 * @description:
 */
public class AA extends SkinBaseActivity {
    @Override
    public void initView() {

    }

    public void loadData1(View view) {
        Intent intent = new Intent(this, AA.class);
        startActivity(intent);
    }

    public void loadData2(View view) {
        Intent intent = new Intent(this, BB.class);
        startActivity(intent);
    }

    public void loadData3(View view) {
        Intent intent = new Intent(this, CC.class);
        startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.aa;
    }
}
