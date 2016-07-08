package com.xhly.leave.event;

import com.xhly.leave.model.Student;

/**
 * Created by 新火燎塬 on 2016/7/3. 以及  on 11:16!^-^
 */
public class NewMessage {
    private Student student;

    public NewMessage() {
    }

    public NewMessage(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
