package com.xhly.leave.model;

import android.text.TextUtils;

import com.xhly.leave.util.Constants;
import com.xhly.leave.util.TimeUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 新火燎塬 on 2016/7/1. 以及  on 16:17!^-^
 */
@Table(name = "student")
public class Student implements Serializable{
    @Column(name = "_id", isId = true)
    private int _id;
    @Column(name = "name")
    private String name;
    @Column(name = "number")
    private String number;
    @Column(name = "_class")
    private String _class;
    @Column(name = "buildDate")
    private long buildDate;
    @Column(name = "startDate")
    private long startDate;
    @Column(name = "endDate")
    private long endDate;
    @Column(name = "reasonType")
    private int reasonType;
    @Column(name = "desc")
    private String desc = "无";
    @Column(name = "handlerType")
    private int handlerType = 0;
    private boolean isChecked = false;

    public Student() {
    }

    public Student(int _id, String _class, String name, String number,  long buildDate, long startDate, long endDate, int reasonType, String desc) {
        this._id = _id;
        this.name = name;
        this.number = number;
        this._class = _class;
        this.buildDate = buildDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reasonType = reasonType;
        if(!TextUtils.isEmpty(desc.trim())){
            this.desc = desc;
        }
    }

    public Student(int _id, String name, String number, String _class, long buildDate, long startDate, long endDate, int reasonType, String desc, int handlerType) {
        this._id = _id;
        this.name = name;
        this.number = number;
        this._class = _class;
        this.buildDate = buildDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reasonType = reasonType;
        this.desc = desc;
        this.handlerType = handlerType;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public long getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(long buildDate) {
        this.buildDate = buildDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getReasonType() {
        return reasonType;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(int handlerType) {
        this.handlerType = handlerType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return _class + "班\n"+
                "请假时间:\n从" + TimeUtil.dateToLocal(new Date(startDate)) +
                "到" + TimeUtil.dateToLocal(new Date(endDate)) +"\n"+
                "原因:" + Constants.reason[reasonType]+ "-"+ desc ;
    }
}
