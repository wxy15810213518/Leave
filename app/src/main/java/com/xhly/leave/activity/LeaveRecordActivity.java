package com.xhly.leave.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xhly.leave.R;

import at.markushi.ui.ActionView;

public class LeaveRecordActivity extends Activity {

    private ActionView action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_record);
        action = (ActionView) findViewById(R.id.action);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeaveRecordActivity.this,AskForLeaveActivity.class));
            }
        });
    }
}
