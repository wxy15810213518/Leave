package com.xhly.leave.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xhly.leave.R;

/**
 * Created by 新火燎塬 on 2016/7/3. 以及  on 18:43!^-^
 */
public class OptionFragment extends Fragment {

    private CheckBox cb_notification;
    private LinearLayout ll_cb_notification;
    private LinearLayout ll_templete;
    private LinearLayout ll_about;
    private LinearLayout ll_suggest;
    private LinearLayout ll_update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.option_layout, null);
        cb_notification = (CheckBox) view.findViewById(R.id.cb_notification);
        ll_cb_notification = (LinearLayout) view.findViewById(R.id.ll_cb_notification);

        ll_templete = (LinearLayout) view.findViewById(R.id.ll_templete);
        ll_about = (LinearLayout) view.findViewById(R.id.ll_about);
        ll_suggest = (LinearLayout) view.findViewById(R.id.ll_suggest);
        ll_update = (LinearLayout) view.findViewById(R.id.ll_update);


        if (getActivity().getSharedPreferences("qjconfig", Context.MODE_PRIVATE).getBoolean("isNotificated", true)) {
            cb_notification.setChecked(true);
        } else {
            cb_notification.setChecked(false);
        }

        ll_cb_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_notification.setChecked(!cb_notification.isChecked());
                if (cb_notification.isChecked()) {
                    getActivity().getSharedPreferences("qjconfig", Context.MODE_PRIVATE).edit().putBoolean("isNotificated", true).commit();
                } else {
                    getActivity().getSharedPreferences("qjconfig", Context.MODE_PRIVATE).edit().putBoolean("isNotificated", false).commit();
                }
            }
        });

        ll_templete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View inflate = View.inflate(getActivity(), R.layout.sms_templete_dialog, null);
                final EditText et_agree = (EditText) inflate.findViewById(R.id.et_agree);
                final EditText et_refuse = (EditText) inflate.findViewById(R.id.et_refuse);
                new AlertDialog.Builder(getActivity())
                            .setTitle("短信模版编辑")
                            .setView(inflate)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity()
                                        .getSharedPreferences("qjconfig",Context.MODE_PRIVATE)
                                        .edit().putString("agreeTemp",et_agree.getText().toString().trim()).commit();

                                getActivity()
                                        .getSharedPreferences("qjconfig",Context.MODE_PRIVATE)
                                        .edit().putString("refuseTemp",et_refuse.getText().toString().trim()).commit();

                            }
                        })
                        .setNegativeButton("取消", null)
                            .show();
            }
        });

        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                            .setTitle("关于软件")
                            .setMessage("本软件自主研发,用于请假记录,请合理使用！")
                            .show();
            }
        });

        ll_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                            .setTitle("建议反馈")
                            .setMessage("正在完善,部分功能尚不完善!")
                            .show();
            }
        });

        ll_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                            .setTitle("版本更新")
                            .setMessage("当前版本 V1.0\n已经是最新版本")
                            .show();
            }
        });
        return view;
    }
}
