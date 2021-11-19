package com.analysis.logman.dao;

import com.analysis.logman.entity.FwlogflowEntity;
import com.analysis.logman.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 防火墙流量的db操作
 */
public class FwlogFlowDao {
    String hql = null;
    //    获取某个时间段之前的所有数据
    public List<FwlogflowEntity> getFwlogFlowByTime(Date time){

        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<FwlogflowEntity> list = new ArrayList<>();
        try{
            hql = "from FwlogflowEntity fe where fe.starttime <= ? order by fe.starttime desc";
            Query query = session.createQuery(hql);
            query.setParameter(0,time);
            list = query.list();
            tx.commit();
        }catch (Exception e){
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session.isOpen() && session != null){
                session.close();
            }
        }
        return list;
    }
    //    更新流量异常分析的结果
    public void updateById(int abnormal,Long Id){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{
            hql="update FwlogflowEntity fe set fe.abnormal = ? where fe.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0,abnormal);
            query.setParameter(1,Id);
            query.executeUpdate();
            tx.commit();
        }catch (Exception e){
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }
    //    更新实时流量
    public void update(int curStream,Date time){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{
            hql="update FwlogflowEntity fe set fe.curstream = ? where fe.starttime = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0,curStream);
            query.setParameter(1,time);
            query.executeUpdate();
            tx.commit();
        }catch (Exception e){
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            if (session.isOpen() && session != null)
                session.close();
        }

    }
}
