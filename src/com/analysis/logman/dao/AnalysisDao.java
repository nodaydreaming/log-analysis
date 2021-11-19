package com.analysis.logman.dao;

import com.analysis.logman.utils.AddressUtils;
import com.analysis.logman.utils.HibernateUtils;
import com.analysis.logman.utils.IPChangeUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AnalysisDao {
    //返回介于时间端内的防火墙日志
    public List findFwlogByTime(Date starttime,Date endtime){
        Session session = null;
        Transaction tx = null;
        List list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String st = sdf.format(starttime);
            String et = sdf.format(endtime);
            session = HibernateUtils.getInstance().getSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from FwlogEntity"
                    + " as T where T.producetime>='"+st
                    + "' and T.producetime<='"+et+"'");

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
    //返回tableName表中介于starttime和endtime之间的所有数据
    public List findAllByTime(String tableName,Date st,Date et){

        Session session = null;
        Transaction tx = null;
        List list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String starttime = sdf.format(st);
            String endtime = sdf.format(et);
            session = HibernateUtils.getInstance().getSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from "+tableName
                    + " as T where T.starttime>='"+starttime
                    + "' and T.endtime<='"+endtime+"'");

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
    //返回未分析过的流量集合
    public List findAllByAbnormal(String tableName){
        Session session = null;
        Transaction tx = null;
        List list = null;
        try{
            session = HibernateUtils.getInstance().getSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from "+tableName
                    + " as T where T.abnormal=-1");

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
    //返回tableName表中，访问时间介于st和et的，源Ip的所有访问记录
    public List findBySrcIP(String tableName, Long SrcIp, Date st, Date et) {
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String starttime = sdf.format(st);
            String endtime = sdf.format(et);
            if(st!=null&&et!=null) {
                Query query = session.createQuery("from " + tableName
                        + " as t where t.producetime>='"+starttime
                        + "' and t.producetime<='"+endtime+"'"
                        + " and t.srcip="+SrcIp);
                list = query.getResultList();
            }
            else{
                Query query = session.createQuery("from " + tableName
                        + " as t where t.srcip=" + SrcIp);
                list = query.getResultList();
            }
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
    //返回tableName表中，访问时间介于st和et的，目的Ip的所有被访问记录
    public List findByDestIP(String tableName,Long destIp,Date st,Date et){

        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String starttime = sdf.format(st);
            String endtime = sdf.format(et);
            Query query = session.createQuery("from " + tableName
                    + " as t where t.producetime>='"+starttime
                    + "' and t.producetime<='"+endtime+"'"
                    + " and t.destip="+destIp);
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
    //返回tableame表中，访问时间介于st和et的，Name元组的TOP10name（如srcip访问次数的TOP10的ip）
    public List findTop10IP(String tableName,String Name, Date st, Date et){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        try{
            String starttime = sdf.format(st);
            String endtime = sdf.format(et);
            if(st!=null&&et!=null) {

                Query query = session.createQuery("select " + Name
                        + " from " + tableName
                        + " as t where t.producetime>='"+starttime
                        + "' and t.producetime<='"+endtime
                        + "' group by " + Name
                        + " order by count(*) desc");
                query.setMaxResults(10);

                list = query.getResultList();
            }
            else{
                Query query = session.createQuery("select " + Name
                        + " from " + tableName
                        + " group by " + Name
                        + " order by count(*) desc");
                query.setMaxResults(10);

                list = query.getResultList();
            }
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
    //返回对应源Ip、目的Ip，并且介于starttime和endtime之间的数据
    public List findByIPAndTime(String tableName,Long srcip,Long destip,Date starttime,Date endtime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        try{
            if(starttime!=null&&endtime!=null) {

                Query query = session.createQuery("from " + tableName+" as t"
                        + " where t.srcip="+srcip
                        + " and t.destip="+destip
                        + " and t.producetime>='"+starttime
                        + "' and t.producetime<='"+endtime+"'");

                list = query.getResultList();
            }
            else{
                Query query = session.createQuery("from " + tableName+" as t"
                        + " where t.srcip="+srcip
                        + " and t.destip="+destip);
                list = query.getResultList();
            }
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
    //查询port是否是敏感端口
    public List findByPort(String tableName,int port){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        try{
            Query query = session.createQuery("from " + tableName
                    + " as t where t.port="+port);

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
    //更新原始防火墙日志的源IP地址
    public void updateFwlogAddress(long a,String address){

        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{

            String hql1 = "UPDATE FwlogEntity SET address ='"+address+"' WHERE id="+a;
            Query query1 = session.createQuery(hql1);
            query1.executeUpdate();
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

    public void updateWeblogURL(int id,String URL){

        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        AddressUtils addressUtils = new AddressUtils();
        IPChangeUtil ipChangeUtil = new IPChangeUtil();
        try{

            String hql1 = "UPDATE WeblogeventsEntity as w SET w.attackaddress = '"+URL
                        +"' WHERE id="+id;
            Query query1 = session.createQuery(hql1);
            query1.executeUpdate();
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
    //返回所有未被分析的web日志
    public List webFindAll(String tableName){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        List list = null;
        try{
            Long maxId = 0L;
            Query query = session.createQuery("select distinct max(weblogid) from WeblogeventsEntity");
            List list1 = query.getResultList();
            if(list1.get(0) != null)
                maxId = (Long)list1.get(0);
            Query query1 = session.createQuery("from " + tableName
                    + " as t where t.id > "+maxId+" and t.analysis = 0");
            list = query1.getResultList();
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
    //设置web日志是否被分析过
    public void setAnalysis(){
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        AddressUtils addressUtils = new AddressUtils();
        IPChangeUtil ipChangeUtil = new IPChangeUtil();
        try{

            String hql1 = "UPDATE WeblogstatisticsEntity as w SET w.analysis = 1";
            Query query1 = session.createQuery(hql1);
            query1.executeUpdate();
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
    //返回这段事件tableName表中的日志事件
    public List findEventsBytime(String tableName,Date starttime,Date endtime){
        List list = null;
        Session session = HibernateUtils.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try{

            String hql1 = "select * "+tableName+" where starttime >= "+starttime+" and endtime <= "+endtime;
            Query query1 = session.createQuery(hql1);
            query1.executeUpdate();
            tx.commit();
        }catch (Exception e){
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return list;
    }
}
