package com.xhly.leave.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xhly.leave.R;
import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.event.NewMessage;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.ClipUtils;
import com.xhly.leave.util.NotificationUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddLeaveActivity extends Activity {

    private EditText et_paste_sms;
    private EditText et_class;
    private EditText et_name;
    private EditText et_reason;
    private Spinner spinner;
    private long startDate = 0;
    private long endDate = 0;
    private int reasonType=0;
    private LinearLayout ll_startDate;
    private LinearLayout ll_endDate;
    private SimpleDateFormat sdf;
    private long minDate;
    private DatePicker datePicker;
    private View dateTimeView;
    private TimePicker timePicker;
    private EditText et_endDate;
    private EditText et_startDate;

    //		Student stu = new Student(0, _class, name, number, new Date().getTime(), startDate, endDate, reasonType, desc);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_leave);
        et_paste_sms = (EditText) findViewById(R.id.et_paste_sms);
        et_class = (EditText) findViewById(R.id.et_class);
        et_name = (EditText) findViewById(R.id.et_name);
        et_reason = (EditText) findViewById(R.id.et_reason);
        spinner = (Spinner) findViewById(R.id.spinner);
        ll_startDate = (LinearLayout) findViewById(R.id.ll_startDate);
        ll_endDate = (LinearLayout) findViewById(R.id.ll_endDate);
        et_startDate = (EditText) findViewById(R.id.et_startDate);
        et_endDate = (EditText) findViewById(R.id.et_endDate);


        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            minDate = sdf.parse("2016-1-1").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        et_paste_sms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String paste = ClipUtils.paste(AddLeaveActivity.this);
                et_paste_sms.setText(paste);
                return true;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/**
 *
 */
        ll_startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dateTimeView = View.inflate(AddLeaveActivity.this, R.layout.date_time_dialog_layout, null);
                datePicker = (DatePicker) dateTimeView.findViewById(R.id.datePicker);
                timePicker = (TimePicker) dateTimeView.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);

                datePicker.setMinDate(minDate);

                new AlertDialog.Builder(AddLeaveActivity.this)
                            .setTitle("开始时间")
                            .setView(dateTimeView)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    et_startDate.setText(datePicker.getYear() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getDayOfMonth() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    try {
                                        startDate = sdf.parse(et_startDate.getText().toString()).getTime();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
            }
        });
        ll_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimeView = View.inflate(AddLeaveActivity.this, R.layout.date_time_dialog_layout, null);
                datePicker = (DatePicker) dateTimeView.findViewById(R.id.datePicker);
                timePicker = (TimePicker) dateTimeView.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);

                datePicker.setMinDate(minDate);
                new AlertDialog.Builder(AddLeaveActivity.this)
                        .setTitle("开始时间")
                        .setView(dateTimeView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_endDate.setText(datePicker.getYear() + "-" + (datePicker.getMonth() +1)+ "-" + datePicker.getDayOfMonth() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                try {
                                    endDate = sdf.parse(et_endDate.getText().toString()).getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    public void pasteOk(View v) {
       String messageBody =  et_paste_sms.getText().toString();
        if (messageBody!=null&&messageBody.length()>0) {
            if(messageBody.startsWith("*#")&&messageBody.endsWith("*#")){
                messageBody = messageBody.substring(2, messageBody.length() - 2);
                String[] split = messageBody.split("-");
                String _class = split[0];
                String name = split[1];
                String number = "";
                long startDate =Long.parseLong(split[2]);
                long endDate = Long.parseLong(split[3]);
                int  reasonType = Integer.parseInt(split[4]);
                String desc = split[5];
                Student stu = new Student(0, _class, name, number, new Date().getTime(), startDate, endDate, reasonType, desc);
                stu.setHandlerType(1);//0未处理 1同意 2拒绝
                new StudentDao().save(stu);
                EventBus.getDefault().post(new NewMessage(stu));

                if(getSharedPreferences("qjconfig", Context.MODE_PRIVATE).getBoolean("isNotificated",true)){
                    NotificationUtils.notification(this, stu.getName());
                }
            }
        }
    }
    
    public void addOk(View v) {
        String _class =null;
        String name = null;
        String reason = null;

        if(et_class.getText().toString().trim().length()==0){
            Toast.makeText(this, "请填写班级", Toast.LENGTH_SHORT).show();
            return;
        }
        if(et_name.getText().toString().trim().length()==0){
            Toast.makeText(this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(et_reason.getText().toString().trim().length()==0) {
            Toast.makeText(this, "请填写原因", Toast.LENGTH_SHORT).show();
            return;
        }
        if(startDate==0){
            Toast.makeText(this, "请填写开始时间", Toast.LENGTH_SHORT).show();
            return;
    }
        if(endDate ==0){
            Toast.makeText(this, "请填写结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        _class = et_class.getText().toString().trim();
        name = et_name.getText().toString().trim();
        reason = et_reason.getText().toString().trim();


        Student stu = new Student(0, _class, name, "", new Date().getTime(), startDate, endDate, reasonType, reason);
        stu.setHandlerType(1);//0未处理 1同意 2拒绝
        new StudentDao().save(stu);
        EventBus.getDefault().post(new NewMessage(stu));

        if(getSharedPreferences("qjconfig", Context.MODE_PRIVATE).getBoolean("isNotificated",true)){
            NotificationUtils.notification(this, stu.getName());
        }
    }



    
}


