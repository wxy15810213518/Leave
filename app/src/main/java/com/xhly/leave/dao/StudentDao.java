package com.xhly.leave.dao;

import com.xhly.leave.model.Student;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 新火燎塬 on 2016/7/1. 以及  on 22:18!^-^
 */
public class StudentDao {
    private DbManager.DaoConfig config;


    public StudentDao() {
        config = new DbManager.DaoConfig()
                .setDbName("qjdb.db")
                .setDbVersion(1);
    }
    /**
     * 返回包含所有记录的对象集合
     *
     * @return
     */
    public List<Student> getAll(){
        List<Student> list = null;
        DbManager db = null;
        try {
            db = x.getDb(config);
            list = db.findAll(Student.class);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(list==null){
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     * 返回包含所有记录的对象集合
     *
     * @return
     */

   public List<Student> getAllByName(String name){
        List<Student> list = null;
        DbManager db = null;
        try {
            db = x.getDb(config);
            list = db.selector(Student.class)
                    .where("name", "=", name)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(list==null){
            list = new ArrayList<>();
        }
        return list;
    }

   public List<Student> getAllByDate(long startDate ,long endDate){
        List<Student> list = null;
        DbManager db = null;
        try {
            db = x.getDb(config);
            list = db.selector(Student.class)
                    .where("startDate", "<=", endDate)
                    .and("endDate",">=",startDate)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(list==null){
            list = new ArrayList<>();
        }
        return list;
    }

/*   public List<Student> getAllByName(String name){
        List<Student> list = null;
        DbManager db = null;
        try {
            db = x.getDb(config);
            list = db.findAll(Student.class);

            for (int i = 0; i < list.size(); i++) {

                if(!list.get(i).getName().equals(name)){
                    list.remove(i);
                    i--;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(list==null){
            list = new ArrayList<>();
        }

        return list;
    }*/

   public List<DbModel> getAllName(){
        List<DbModel> list = null;
        DbManager db = null;
        try {
            db = x.getDb(config);
            list = db.selector(Student.class)
                    .groupBy("name")
                    .orderBy("buildDate",true)
                    .select("name", "count(name) as count").findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(list==null){
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     *
     * @param student
     */
    public void save(Student student) {
        DbManager db = null;
        try {
            db = x.getDb(config);
            //将数据插入表, 同时将生成的id设置到对象中
            db.saveBindingId(student);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *
     * @param student
     */
    public void update(Student student) {
        DbManager db = null;
        try {
            db = x.getDb(config);
            //将数据插入表, 同时将生成的id设置到对象中
            db.update(Student.class, WhereBuilder.b("_id", "=", student.get_id()), new KeyValue("handlerType", student.getHandlerType()));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param student
     */
    public void delete(Student student) {
        DbManager db = null;
        try {
            db = x.getDb(config);
            //将数据插入表, 同时将生成的id设置到对象中
            db.delete(Student.class, WhereBuilder.b("_id", "=", student.get_id()));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
