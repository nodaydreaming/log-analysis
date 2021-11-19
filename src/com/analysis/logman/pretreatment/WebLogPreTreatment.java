package com.analysis.logman.pretreatment;

import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.entity.WeblogEntity;
import com.analysis.logman.entity.WeblogstatisticsEntity;
import com.analysis.logman.read.ReadWebLog;
import com.analysis.logman.utils.ListMapUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对于web日志的预处理
 * 预统计
 */
public class WebLogPreTreatment {
    //web日志的预统计时间间隔
    private int WEBLOG_TIME_INTERVAL = 5 * 60 * 1000;
    //web日志内容
    private List<WeblogEntity> weblogEntities = new LinkedList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ListMapUtil listMapUtil = new ListMapUtil();
    private DefaultDao defaultDao = new DefaultDao();
    private String sTime = null;
    private String eTime = null;

    public void preTreamentWebLog(File file){
        try{
            weblogEntities = new ReadWebLog().webLogRead(file);
            //list转变成map
            Map<Long,List<WeblogEntity>> webLogMap = listMapUtil.makeEntityListMap(weblogEntities,"ip");
            int times = 0;
            for (Long ip:webLogMap.keySet()){
                //获取该IP下的所有日志数据
                List<WeblogEntity> weblogEntityList = webLogMap.get(ip);
                if (weblogEntityList.size() == 1){
                    sTime = eTime = weblogEntityList.get(0).getProducetime();
                    times = 1;
                    WeblogstatisticsEntity weblogstatisticsEntity = new WeblogstatisticsEntity();
                    weblogstatisticsEntity.setBrowser(weblogEntityList.get(0).getBrowser());
                    weblogstatisticsEntity.setEndtime(sdf.parse(eTime));
                    weblogstatisticsEntity.setStarttime(sdf.parse(sTime));
                    weblogstatisticsEntity.setReactnum(weblogEntityList.get(0).getReactnum());
                    weblogstatisticsEntity.setSrcip(ip);
                    weblogstatisticsEntity.setUrl(weblogEntityList.get(0).getUrl());
                    weblogstatisticsEntity.setMethod(weblogEntityList.get(0).getMethod());
                    weblogstatisticsEntity.setTimes(times);
                    defaultDao.insert(weblogstatisticsEntity);
                }else{
//                    统计阈值时间内对IP的访问数量
//                    对于每一列按照时间进行排序
                    Collections.sort(weblogEntityList);
//                    统计时间阈值内，该IP访问的相同的url的次数
//                    根据url进行分类
                    Map<String,List<WeblogEntity>> map = listMapUtil.makeEntityListMap(weblogEntityList,"url");
                    for (String url : map.keySet()){

                        //根据url分类
                        List<WeblogEntity> list = map.get(url);
                        sTime = eTime =list.get(0).getProducetime();
                        if (list.size() == 1){
                            Date st = sdf.parse(sTime);
                            Date et = sdf.parse(eTime);
                            int reactNum = list.get(0).getReactnum();
                            String method = list.get(0).getMethod();
                            String browser = list.get(0).getBrowser();
                            times = 1;
                            WeblogstatisticsEntity weblogstatisticsEntity = new WeblogstatisticsEntity();
                            weblogstatisticsEntity.setTimes(times);
                            weblogstatisticsEntity.setUrl(url);
                            weblogstatisticsEntity.setSrcip(ip);
                            weblogstatisticsEntity.setStarttime(st);
                            weblogstatisticsEntity.setEndtime(et);
                            weblogstatisticsEntity.setMethod(method);
                            weblogstatisticsEntity.setBrowser(browser);
                            weblogstatisticsEntity.setReactnum(reactNum);
                            defaultDao.insert(weblogstatisticsEntity);
                            times = 0;
                        }else{
                            for (WeblogEntity weblogEntity:list){
                                times++;
                                eTime = weblogEntity.getProducetime();
                                if (sdf.parse(eTime).getTime() - sdf.parse(sTime).getTime() > WEBLOG_TIME_INTERVAL){
                                    Date st = sdf.parse(sTime);
                                    Date et = sdf.parse(eTime);
                                    String browser = list.get(0).getBrowser();
                                    int reactNum = list.get(0).getReactnum();
                                    String method = list.get(0).getMethod();
                                    WeblogstatisticsEntity weblogstatisticsEntity = new WeblogstatisticsEntity();
                                    weblogstatisticsEntity.setTimes(times);
                                    weblogstatisticsEntity.setUrl(url);
                                    weblogstatisticsEntity.setSrcip(ip);
                                    weblogstatisticsEntity.setStarttime(st);
                                    weblogstatisticsEntity.setEndtime(et);
                                    weblogstatisticsEntity.setMethod(method);
                                    weblogstatisticsEntity.setBrowser(browser);
                                    weblogstatisticsEntity.setReactnum(reactNum);
                                    defaultDao.insert(weblogstatisticsEntity);
                                    sTime = eTime;
                                    times = 0;
                                }
                            }
                        }
                    }
                }
            }

        }catch (ParseException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WebLogPreTreatment().preTreamentWebLog(new File("E:\\log\\access_.log"));
    }

}
