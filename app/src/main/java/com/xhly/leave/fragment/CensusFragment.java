package com.xhly.leave.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.xhly.leave.R;
import com.xhly.leave.activity.CensusActivity;
import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 新火燎塬 on 2016/7/5. 以及  on 23:26!^-^
 */
public class CensusFragment extends Fragment {

    private View view;
    private TextView tv_start_date;
    private TextView tv_stop_date;
    private DatePicker datePicker;
    private Button btn_ok;
    private long startDate;
    private long minDate;
    private SimpleDateFormat sdf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.census_layout, null);
        tv_start_date = (TextView) view.findViewById(R.id.tv_start_date);
        tv_stop_date = (TextView) view.findViewById(R.id.tv_stop_date);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setEnabled(false);

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            minDate = sdf.parse("2016-1-1").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker = new DatePicker(getActivity());
                datePicker.setCalendarViewShown(false);
                datePicker.setMinDate(minDate);
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择时间")
                        .setView(datePicker)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    startDate = sdf.parse(datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+(datePicker.getDayOfMonth() + 1)).getTime();

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_start_date.setText(" " + datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                                try {
                                    tv_start_date.setTag(sdf.parse(datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth()).getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_stop_date.setText("选择日期");
                                btn_ok.setEnabled(false);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        tv_stop_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_start_date.getText().toString().startsWith(" ")){
                    datePicker = new DatePicker(getActivity());
                    datePicker.setMinDate(startDate);
                    datePicker.setCalendarViewShown(false);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("请选择时间")
                            .setView(datePicker)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Date date = null;
                                    btn_ok.setEnabled(true);
                                    int year = datePicker.getYear();
                                    int month =  datePicker.getMonth();
                                    int day =  datePicker.getDayOfMonth();
                                    try {
                                        date = sdf.parse(year + "-" + month + "-" + day);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    tv_stop_date.setTag(date.getTime());
                                    tv_stop_date.setText(" "+datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth());
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }else{
                    Toast.makeText(getActivity(), "请先选择开始时间", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Student> allByDate = new StudentDao().getAllByDate((long) tv_start_date.getTag(), (long) tv_stop_date.getTag());
                for (int i = 0; i < allByDate.size(); i++) {
                    Log.e("TAG", allByDate.get(i)+"");
                }
                if(allByDate!=null&&allByDate.size()>0){
                    Intent intent = new Intent(getActivity(), CensusActivity.class);
                    intent.putExtra("list",(ArrayList)allByDate);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "没有找到记录", Toast.LENGTH_SHORT).show();
                }
                     }
        });

        return view;
    }
}
