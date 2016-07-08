package com.xhly.leave.receiver;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.event.NewMessage;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.NotificationUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;


public class SmsReceiver extends BroadcastReceiver {
	private DevicePolicyManager mDPM;
	private ComponentName who;



	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundleExtra = intent.getExtras();
		Object[] object = (Object[]) bundleExtra.get("pdus");

		for (Object obj : object) {
			SmsMessage createFromPdu = SmsMessage
					.createFromPdu((byte[]) obj);
			String originatingAddress = createFromPdu.getOriginatingAddress();
			String messageBody = createFromPdu.getMessageBody();
			// System.err.println("短信来了--------------------***********"+originatingAddress+"-----"+messageBody);
//		*#0410-姓名-电话-创建时间-开始时间-结束时间-请假总时间 -请假类型-请假原因*#
			if (messageBody!=null&&messageBody.length()>0) {
				if(messageBody.startsWith("*#")&&messageBody.endsWith("*#")){
					messageBody = messageBody.substring(2, messageBody.length() - 2);
					String[] split = messageBody.split("-");
					String _class = split[0];
					String name = split[1];
					String number = originatingAddress;
					long startDate =Long.parseLong(split[2]);
					long endDate = Long.parseLong(split[3]);
					int  reasonType = Integer.parseInt(split[4]);
					String desc = split[5];
					Student stu = new Student(0, _class, name, number, new Date().getTime(), startDate, endDate, reasonType, desc);
					new StudentDao().save(stu);
					EventBus.getDefault().post(new NewMessage(stu));
					abortBroadcast();

					if(context.getSharedPreferences("qjconfig",Context.MODE_PRIVATE).getBoolean("isNotificated",true)){
						NotificationUtils.notification(context,stu.getName());
					}
				}
			}
		}
	}
}

