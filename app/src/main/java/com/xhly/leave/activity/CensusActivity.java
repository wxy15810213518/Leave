package com.xhly.leave.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhly.leave.R;
import com.xhly.leave.base.CommonBaseAdapter;
import com.xhly.leave.base.ViewHolder;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.Constants;
import com.xhly.leave.util.PinYinUtils;
import com.xhly.leave.util.TimeUtil;

import java.util.ArrayList;

public class CensusActivity extends Activity {

    private RecyclerView rv;
    private ArrayList<Student> data;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census);

        intent = getIntent();

        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter() {

        rv.setAdapter(new CommonBaseAdapter(this, data, R.layout.qj_item_layout) {
            @Override
            public void convert(ViewHolder holder, int position) {
                final Student student = data.get(position);
                holder.setText(R.id.tv_name, student.getName());
                holder.setText(R.id.tv_detail, student.toString());
                holder.setText(R.id.tv_build_time, TimeUtil.getChatTime(true, student.getBuildDate()));
                holder.setImageResource(R.id.iv_icon, Constants.icon[getFirstLetter(student.getName())]);
                holder.setOnclickListener(R.id.iv_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CensusActivity.this, StudentActivity.class);
                        intent.putExtra("student", student);
                        startActivity(intent);
                    }
                });
                if (student.getHandlerType() == 1) {
                    holder.setVisibility(R.id.iv_type, View.VISIBLE);
                    holder.setImageResource(R.id.iv_type, R.drawable.agree1);
                } else if (student.getHandlerType() == 2) {
                    holder.setVisibility(R.id.iv_type, View.VISIBLE);
                    holder.setImageResource(R.id.iv_type, R.drawable.refuse);
                } else if (student.getHandlerType() == 0) {
                    holder.setVisibility(R.id.iv_type, View.GONE);
                }
            }
        });
    }

    private void initData() {
       data = (ArrayList<Student>) intent.getSerializableExtra("list");

    }

    private int getFirstLetter(String name) {
        int index = 0;
        char first = name.charAt(0);
        if(first>='A'&&first<='Z'){
            index = first -'A';
        }else if(first>='a'&&first<='z'){
            index = first -'a';
        }else {
            index = PinYinUtils.getPinYin(name).charAt(0) - 'A';
        }
        return index;
    }
}
