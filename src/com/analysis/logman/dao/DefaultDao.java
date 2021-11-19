package com.analysis.logman.dao;

import com.analysis.logman.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

//该类通用函数的保存，insert...
public class DefaultDao {
    //插入数据
    public void insert(Object object){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{
            session.save(object);
            tx.commit();
        }catch (Exception e){
//            回滚
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }

    }
    //返回tableName表中的所有记录
    public List findAll(String tableName){

        Session session = null;
        Transaction tx = null;
        List list = null;
        try{
            session = HibernateUtils.getInstance().getSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("from "+tableName);

            list = query.getResultList();

            tx.commit();
        } catch (Exception e){
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return list;
    }

}
