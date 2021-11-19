package com.analysis.logman.dao;

import com.analysis.logman.entity.WebflowavgEntity;
import com.analysis.logman.entity.WeblogflowEntity;
import com.analysis.logman.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 对于web流量的db操作
 * @author jamesZhan
 */
public class WebFlowDao {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String hql = null;
    //    查询获得这个时间点之前的所有平均流量
    public List<Integer> getWeblogByTime(Date st){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<Integer> result = new ArrayList<>();
        try{
            hql="select fe.avgflow from WebflowavgEntity fe where fe.starttime <= ? order by fe.starttime desc";
            Query query = session.createQuery(hql);
            query.setParameter(0,st);
            result = query.list();
            tx.commit();
        }catch (Exception e){
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session.isOpen() && session != null)
                session.close();
        }
        return result;
    }
    //    查询这个时间点之前的几个瞬时流量
    public List<WeblogflowEntity> getInstantFlow(Date st){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<WeblogflowEntity> result = new LinkedList<>();
        try {
            Query query = session.createQuery("from WeblogflowEntity fe where fe.starttime <= ?");
            query.setParameter(0,st);
            result = query.list();
            tx.commit();
        }catch (Exception e){
            if (tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            if (session.isOpen() && session != null)
                session.close();
            return result;
        }
    }
    //    更新实时的流量
    public void update(int curStream, Date starttime){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{
            hql="update WeblogflowEntity fe set fe.curstream = ? where fe.starttime = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0,curStream);
            query.setParameter(1,starttime);
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
    //    更新流量异常分析的结果
    public void updateById(int abnormal,Long Id){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{
            hql="update WeblogflowEntity fe set fe.abnormal = ? where fe.id = ?";
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
    //        获取数据表当中的第一条数据
    public WeblogflowEntity getFirstData(){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        WeblogflowEntity weblogflowEntity = new WeblogflowEntity();
        try {
            Query query = session.createQuery("from WeblogflowEntity wfae order by wfae.starttime");
            List<WeblogflowEntity> weblogflowEntities = query.list();
            weblogflowEntity = weblogflowEntities.get(0);
            tx.commit();
        }catch (Exception e){
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session.isOpen())
                session.close();
            return weblogflowEntity;
        }
    }
    //    获取某天某时刻的流量
    public WeblogflowEntity getFlowByTime(String time){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<WeblogflowEntity> weblogflowEntities = new LinkedList<>();
        try{
            Query query = session.createQuery("from WeblogflowEntity fe where fe.starttime >= ?");
            query.setParameter(0,sdf.parse(time));
            weblogflowEntities = query.list();
        }catch(Exception e){
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session.isOpen() && session != null){
                session.close();
            }
            return weblogflowEntities.get(0);
        }

    }

    public List<WeblogflowEntity> getFlow(){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List<WeblogflowEntity> flowEntities = new ArrayList<>();
        try{
            String hql = "from WeblogflowEntity fe order by fe.starttime desc";
            Query query = session.createQuery(hql);
            query.setFirstResult(0);
            query.setMaxResults(15);
            flowEntities = query.list();
        }catch (Exception e){
            if (tx != null)
                tx.rollback();
        }finally {
            if (session.isOpen())
                session.close();
            return flowEntities;
        }
    }

    public WebflowavgEntity getLastData(){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        WebflowavgEntity webflowavgEntity = new WebflowavgEntity();
        try{
            String hql = "from WebflowavgEntity w order by w.starttime desc";
            Query query = session.createQuery(hql);
            webflowavgEntity = (WebflowavgEntity) query.setFirstResult(0).list().get(0);
        }catch (Exception e){
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session.isOpen())
                session.close();
            return webflowavgEntity;
        }
    }


    public static void main(String[] args) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WebFlowDao webFlowDao = new WebFlowDao();
        String time = "2016-09-06 21:56:00";
//        webFlowDao.update(5193,simpleDateFormat.parse(time));
        List<Integer> list = webFlowDao.getWeblogByTime(simpleDateFormat.parse(time));
//        WebflowavgEntity flowEntity = webFlowDao.getLastData();
//        System.out.println(flowEntity);
        List<WeblogflowEntity> weblogflowEntities = webFlowDao.getFlow();
        for (WeblogflowEntity weblogflowEntity:weblogflowEntities)
            System.out.println(weblogflowEntity);
    }
}
