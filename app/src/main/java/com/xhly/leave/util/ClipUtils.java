package com.xhly.leave.util;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by 新火燎塬 on 2016/7/6. 以及  on 21:04!^-^
 */
public class ClipUtils {
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    public static void copy(String content, Context context)
    {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}
