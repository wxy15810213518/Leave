package com.xhly.leave.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;

import com.xhly.leave.R;
import com.xhly.leave.activity.base.BaseActivity;
import com.xhly.leave.fragment.CensusFragment;
import com.xhly.leave.fragment.ManageFragment;
import com.xhly.leave.fragment.OptionFragment;
import com.xhly.leave.fragment.QJFragment;

import at.markushi.ui.RevealColorView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RevealColorView revealColorView;
    private View selectedView;
    private int backgroundColor;

    private QJFragment qjFragment;
    private Fragment currentFragment;
    private OptionFragment optionFragment;
    private ManageFragment manageFragment;
    private CensusFragment censusFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        revealColorView = (RevealColorView) findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#212121");


        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);

        selectedView = findViewById(R.id.btn_1);

        qjFragment = new QJFragment();
        optionFragment = new OptionFragment();
        manageFragment = new ManageFragment();
        censusFragment = new CensusFragment();
        currentFragment = qjFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, qjFragment).commit();
    }

    @Override
    public void onClick(View v) {
        final int color = getColor(v);
        final Point p = getLocationInView(revealColorView, v);

        if (selectedView == v) {
            //revealColorView.hide(p.x, p.y, backgroundColor, 0, 300, null);
            selectedView = null;
        } else {
            revealColorView.reveal(p.x, p.y, color, v.getHeight() / 2, 340, null);
            selectedView = v;
        }

        switch(v.getId()){
            case R.id.btn_1:
                if(currentFragment!=qjFragment){
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = qjFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, qjFragment).commit();
                }
                break;
            case R.id.btn_2:
                if(currentFragment!=manageFragment){
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = manageFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, manageFragment).commit();
                }
                break;
            case R.id.btn_3:
                if(currentFragment!=censusFragment){
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = censusFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, censusFragment).commit();
                }
                break;
            case R.id.btn_4:
                if(currentFragment!=optionFragment){
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = optionFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, optionFragment).commit();
                }
                break;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}