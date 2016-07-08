package com.xhly.leave.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xhly.leave.R;
import com.xhly.leave.activity.AddLeaveActivity;
import com.xhly.leave.activity.StudentActivity;
import com.xhly.leave.base.CommonBaseAdapter;
import com.xhly.leave.base.ViewHolder;
import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.event.NewMessage;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.Constants;
import com.xhly.leave.util.PinYinUtils;
import com.xhly.leave.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.ActionView;
import at.markushi.ui.action.BackAction;
import at.markushi.ui.action.CloseAction;

/**
 * Created by 新火燎塬 on 2016/7/1. 以及  on 9:18!^-^
 */
public class QJFragment extends Fragment {

    private View view;
    private RecyclerView rv;
    private List<Student> data;
    private RecyclerView.Adapter adapter;
    private boolean isEdit = false;
    private ActionView actionView;
    private ActionView action_plus;
    private Button btn_ok;
    private Button btn_select;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.qj_layout,null);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_select = (Button) view.findViewById(R.id.btn_select);
        actionView = (ActionView) view.findViewById(R.id.action);
        action_plus = (ActionView) view.findViewById(R.id.action_plus);

        initView();
        initData();
        initAdapter();
        init();
        initListener();
        return view;
    }

    private void initListener() {
        action_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddLeaveActivity.class));
            }
        });
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if(!isEdit){
            editToScan();
        }
        adapter.notifyItemRangeChanged(0, data.size());
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    private void init() {

    }

    private void initView() {

        actionView.setAction(new CloseAction(), ActionView.ROTATE_COUNTER_CLOCKWISE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsEdit(!isEdit());
                if (isEdit()) {
                    ((ActionView) v).setAction(new BackAction(), ActionView.ROTATE_COUNTER_CLOCKWISE);
                    btn_ok.setVisibility(View.VISIBLE);
                    btn_select.setVisibility(View.VISIBLE);
                    action_plus.setVisibility(View.GONE);
                    btn_select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllItemChecked(true);
                        }
                    });

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteItem();
                            setIsEdit(false);
                            actionView.setAction(new CloseAction(), ActionView.ROTATE_COUNTER_CLOCKWISE);
                            btn_ok.setVisibility(View.GONE);
                            btn_select.setVisibility(View.GONE);
                            action_plus.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    setIsEdit(false);
                    btn_ok.setOnClickListener(null);
                    btn_select.setOnClickListener(null);
                    ((ActionView) v).setAction(new CloseAction(), ActionView.ROTATE_COUNTER_CLOCKWISE);
                    btn_ok.setVisibility(View.GONE);
                    btn_select.setVisibility(View.GONE);
                    action_plus.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void initData() {
        data = new ArrayList<>();
        data = new StudentDao().getAll();
//        Collections.reverse(data);
    }

    private void initAdapter() {
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CommonBaseAdapter<Student>(getActivity(), data, R.layout.qj_item_layout) {

            @Override
            public void convert(final ViewHolder holder, final int position) {
                final Student student = data.get(position);
                holder.setText(R.id.tv_name, student.getName());
                holder.setText(R.id.tv_detail, student.toString());
                holder.setText(R.id.tv_build_time, TimeUtil.getChatTime(true, student.getBuildDate()));

                holder.setImageResource(R.id.iv_icon, Constants.icon[getFirstLetter(student.getName())]);

                if(!isEdit){
                    holder.setVisibility(R.id.iv_select,View.GONE);
                    if(student.getHandlerType()==0){
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("TAG", position + "click");
                                new AlertDialog.Builder(getActivity())
                                        .setItems(new String[]{"同意", "拒绝"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, final int which) {
                                                dialog.dismiss();
                                                new AlertDialog.Builder(getActivity())
                                                        .setMessage("确定" + ((which == 0) ? "同意" : "拒绝"))
                                                        .setPositiveButton("自定义", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which1) {
                                                                student.setHandlerType((which == 0) ? 1 : 2);
                                                                new StudentDao().update(student);
                                                                adapter.notifyItemChanged(position);
                                                                sendMessage(getActivity(),student.getNumber(),student.getHandlerType()==1?getActivity()
                                                                        .getSharedPreferences("qjconfig",Context.MODE_PRIVATE)
                                                                        .getString("agreeTemp","同意"):getActivity()
                                                                        .getSharedPreferences("qjconfig", Context.MODE_PRIVATE)
                                                                        .getString("refuseTemp", "拒绝"));
                                                            }
                                                        })
                                                        .setNegativeButton("使用模版", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which1) {
                                                                student.setHandlerType((which == 0) ? 1 : 2);
                                                                new StudentDao().update(student);
                                                                adapter.notifyItemChanged(position);
                                                                sendMessageNoJump(getActivity(),student.getNumber(),student.getHandlerType()==1?getActivity()
                                                                        .getSharedPreferences("qjconfig",Context.MODE_PRIVATE)
                                                                        .getString("agreeTemp","同意"):getActivity()
                                                                        .getSharedPreferences("qjconfig", Context.MODE_PRIVATE)
                                                                        .getString("refuseTemp", "拒绝"));

                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .show();
                            }
                        });
                    }else{
                        holder.getConvertView().setOnClickListener(null);
                    }

                    holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Log.e("TAG", position + "longClick");
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("确定删除记录?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new StudentDao().delete(student);
                                            //解决删除条目造成的混乱
                                            data.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            adapter.notifyItemRangeChanged(0, data.size());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            return false;
                        }
                    });
                    holder.setOnclickListener(R.id.iv_icon, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), StudentActivity.class);
                            intent.putExtra("student",student);
                            startActivity(intent);
                        }
                    });
                }else{
                    holder.setVisibility(R.id.iv_select,View.VISIBLE);
                    if(student.isChecked()){
                        holder.setImageResource(R.id.iv_select,R.drawable.blue_selected);
                    }else{
                        holder.setImageResource(R.id.iv_select,R.drawable.blue_unselected);
                    }
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            student.setIsChecked(!student.isChecked());
                            adapter.notifyItemChanged(position);
                        }
                    });
                    holder.getConvertView().setOnLongClickListener(null);
                    holder.setOnclickListener(R.id.iv_icon,null);
                }


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
        };
        rv.setAdapter(adapter);
        rv.scrollToPosition(data.size() - 1);
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

    @Subscribe
    public void onEventMainThread(NewMessage event) {
        String msg = "onEventMainThread收到了消息：" + event;
        data.add(event.getStudent());
        adapter.notifyItemInserted(data.size() - 1);
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        rv.scrollToPosition(data.size() - 1);
        adapter.notifyItemRangeChanged(0, data.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    private void editToScan(){
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIsChecked(false);
        }
    }


    public void deleteItem() {
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).isChecked()){

                new StudentDao().delete(data.get(i));
                data.remove(i);
                adapter.notifyItemRemoved(i);
                adapter.notifyItemRangeChanged(0, data.size());
                i--;
            }
        }
    }

    public void setAllItemChecked(boolean isChecked){
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIsChecked(isChecked);
        }
        adapter.notifyItemRangeChanged(0,data.size());
    }

    private void sendMessage(Context context ,String phone,String content){
       /* SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phone, null, content, null, null);
        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body",content);
        startActivity(intent);
    }
    private void sendMessageNoJump(Context context ,String phone,String content){
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phone, null, content, null, null);
        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

    }
}
