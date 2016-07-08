package com.xhly.leave.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.xhly.leave.R;
import com.xhly.leave.activity.MainActivity;

/**
 * Created by 新火燎塬 on 2016/7/6. 以及  on 21:24!^-^
 */
public class NotificationUtils {
    public static final int NOTIFICATION_FLAG=1;

    public static void notification(Context context,String name){
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        builder.setTicker("最新请假消息");
        builder.setContentTitle(name + ":请假"); //设置标题
        builder.setContentText("点击查看详细内容"); //消息内容
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (context,MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(NOTIFICATION_FLAG, notification); // 通过通知管理器发送通知
    }
}
