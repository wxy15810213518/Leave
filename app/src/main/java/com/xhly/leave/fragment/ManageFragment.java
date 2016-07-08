package com.xhly.leave.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhly.leave.R;
import com.xhly.leave.activity.StudentActivity;
import com.xhly.leave.base.CommonBaseAdapter;
import com.xhly.leave.base.ViewHolder;
import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.Constants;
import com.xhly.leave.util.PinYinUtils;

import org.xutils.db.table.DbModel;

import java.util.List;

/**
 * Created by 新火燎塬 on 2016/7/5. 以及  on 21:11!^-^
 */
public class ManageFragment extends Fragment {

    private View view;
    private RecyclerView rv;
    private List<DbModel> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.manage_layout, null);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initData();

        rv.setAdapter(new CommonBaseAdapter(getActivity(),data,R.layout.manager_item_layout) {
            @Override
            public void convert(ViewHolder holder, final int position) {
                holder.setImageResource(R.id.iv_icon, Constants.icon[getFirstLetter(data.get(position).getString("name"))]);
                holder.setText(R.id.tv_name, data.get(position).getString("name"));
                holder.setText(R.id.tv_cnt,data.get(position).getString("count"));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Student student = new Student();
                        student.setName(data.get(position).getString("name"));
                        Intent intent = new Intent(getActivity(), StudentActivity.class);
                        intent.putExtra("student",student);
                        startActivity(intent);
                    }
                });

            }
        });

        return view;
    }

    private void initData(){
        data =  new StudentDao().getAllName();
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
