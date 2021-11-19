package com.analysis.logman.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by jamesZhan on 2017/7/29.
 */
public class HibernateUtils {
    private HibernateUtils(){}
    private static HibernateUtils instance = new HibernateUtils();

    public static HibernateUtils getInstance(){
        return instance;
    }

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            //创建配置对象，获取hibernate.cfg.xml配置文件的信息
            Configuration config = new Configuration().configure();
            //创建服务注册对象，创建和销毁都相当耗费资源，通常一个系统内一个数据库只创建一个
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            //创建会话工厂对象，类似于JDBC的Connection
            sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public Session getSession(){
        return getSessionFactory().openSession();
    }
}
