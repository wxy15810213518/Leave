package com.xhly.leave.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 新火燎塬 on 2016/7/1. 以及  on 9:45!^-^
 */
public class BaseActivity extends FragmentActivity {
    protected void showMsg(String xMsg){
        Toast.makeText(this, xMsg, Toast.LENGTH_SHORT).show();
    }
    public void openActivity(Class<?> xClass){
        Intent intent = new Intent();
        intent.setClass(this,xClass);
        startActivity(intent);
    }

    public Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }

    public int getColor(View view) {
        return Color.parseColor((String) view.getTag());
    }
}
