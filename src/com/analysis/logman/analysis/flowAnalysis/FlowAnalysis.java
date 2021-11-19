package com.analysis.logman.analysis.flowAnalysis;


import com.analysis.logman.analysis.associationanalysis.AssociationBySrcip;
import com.analysis.logman.analysis.attackAnalysis.FwlogIpAnalysis;
import com.analysis.logman.analysis.attackAnalysis.FwlogPortAnalysis;
import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.FwlogFlowDao;
import com.analysis.logman.dao.WebFlowDao;
import com.analysis.logman.entity.EventsEntity;
import com.analysis.logman.entity.FwlogflowEntity;
import com.analysis.logman.entity.WeblogflowEntity;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//基于已经统计的每分钟流量，进行流量分析
public class FlowAnalysis {
    private DefaultDao defaultDao = new DefaultDao();
    private AnalysisDao analysisDao = new AnalysisDao();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //Web流量分析
    public void WeblogFlowAnalysis(){
        WebFlowDao webFlowDao = new WebFlowDao();
        List<Pair<Date,Date>> list = new ArrayList<>();
        //取出未分析过的每分钟流量集合
        List<WeblogflowEntity> weblogflowEntities = analysisDao.findAllByAbnormal("WeblogflowEntity");
        if(weblogflowEntities!=null) {
            Date st = null;
            Date et = null;
            int flag1=0,flag0=0;
            //遍历每分钟的流量信息
            for (WeblogflowEntity weblogflowEntity : weblogflowEntities) {
                //实际流量大于预测上限流量times倍以上，则认定为流量异常
                int times = 5;
                if (weblogflowEntity.getCurstream() > weblogflowEntity.getUpstream() * times) {
                    if(flag1==0){
                        st = weblogflowEntity.getStarttime();
                    }
                    et = weblogflowEntity.getEndtime();
                    flag1++;
                    weblogflowEntity.setAbnormal(1);
                    webFlowDao.updateById(1, weblogflowEntity.getId());
                }
                else{
                    weblogflowEntity.setAbnormal(0);
                    webFlowDao.updateById(0, weblogflowEntity.getId());
                    flag0++;
                    if(flag0 < 3 && flag0 <= flag1){
                        continue;
                    }
                    else if(flag1!=0){
                        flag0=0;
                        flag1=0;
                        list.add(new Pair<>(st,et));
                    }
                    else{
                        flag0=0;
                    }
                }
            }
        }
        //将流量异常的起止时间存入数据库
        for(int j=0;j<list.size();j++){
            EventsEntity eventsEntity = new EventsEntity();
            eventsEntity.setStarttime(list.get(j).getKey());
            eventsEntity.setEndtime(list.get(j).getValue());
            eventsEntity.setType(6);
            eventsEntity.setDescription("从"+sdf.format(list.get(j).getKey())
                                        +"到"+sdf.format(list.get(j).getValue()
                                        +",共"+(list.get(j).getValue().getTime()-list.get(j).getKey().getTime())/1000
                                        +"秒，Web服务器流量异常。"));
            defaultDao.insert(eventsEntity);
        }
    }
    //防火墙流量分析
    public void FwlogFlowAnalysis(){
        FwlogFlowDao fwlogFlowDao = new FwlogFlowDao();
        List<Pair<Date,Date>> list = new ArrayList<>();
        List<FwlogflowEntity> fwlogflowEntities = analysisDao.findAllByAbnormal("FwlogflowEntity");
        if(fwlogflowEntities!=null) {
            Date st = null;
            Date et = null;
            int flag1=0,flag0=0;
            //遍历每分钟的流量信息
            for (FwlogflowEntity fwlogflowEntity : fwlogflowEntities) {
                //实际流量大于预测上限流量times倍以上，则认定为流量异常
                int times = 5;
                if (fwlogflowEntity.getCurstream() > fwlogflowEntity.getUpstream() * times) {
                    if(flag1==0){
                        st = fwlogflowEntity.getStarttime();
                    }
                    et = fwlogflowEntity.getEndtime();
                    flag1++;
                    flag0=0;
                    fwlogflowEntity.setAbnormal(1);
                    fwlogFlowDao.updateById(1, fwlogflowEntity.getId());
                }
                else{
                    fwlogflowEntity.setAbnormal(0);
                    fwlogFlowDao.updateById(0, fwlogflowEntity.getId());
                    flag0++;
                    if(flag0 < 3 && flag0 <= flag1){
                        continue;
                    }
                    else if(flag1!=0){
                        flag0=0;
                        flag1=0;
                        list.add(new Pair<>(st,et));
                    }
                    else{
                        flag0=0;
                    }
                }
            }
        }
        //将流量异常的起止时间存入数据库
        for(int j=0;j<list.size();j++){
            EventsEntity eventsEntity = new EventsEntity();
            eventsEntity.setStarttime(list.get(j).getKey());
            eventsEntity.setEndtime(list.get(j).getValue());
            eventsEntity.setType(5);
            eventsEntity.setDescription("从"+list.get(j).getKey()
                                        +"到"+list.get(j).getValue()
                                        +",共"+(list.get(j).getValue().getTime()-list.get(j).getKey().getTime())/1000
                                        +"秒，防火墙流量异常。");
            defaultDao.insert(eventsEntity);

            //对流量异常的时间段进行IP扫描和端口扫描的分析
            FwlogPortAnalysis fwlogPortAnalysis = new FwlogPortAnalysis();
            FwlogIpAnalysis fwlogIpAnalysis = new FwlogIpAnalysis();
            AssociationBySrcip associationBySrcip = new AssociationBySrcip();
            //源IP分析
            fwlogIpAnalysis.SrcIpScanAnalysis(list.get(j).getKey(),list.get(j).getValue());
            //目的IP分析
            fwlogIpAnalysis.DestIpAnalysis(list.get(j).getKey(),list.get(j).getValue());
            //目的端口分析
            fwlogPortAnalysis.DestPortScanAnalysis(list.get(j).getKey(),list.get(j).getValue());
            //防火墙日志和web日志关联分析
//            associationBySrcip
        }
    }

    public static void main(String[] args) {
        FlowAnalysis flowAnalysis = new FlowAnalysis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date starttime = null;
        Date endtime = null;
        try {
            starttime = sdf.parse("2016-02-19 00:00:00");
            endtime = sdf.parse("2016-02-19 13:13:02");
            long start = System.currentTimeMillis();
            flowAnalysis.WeblogFlowAnalysis();
            long end = System.currentTimeMillis();
            System.out.println((end-start)/1000);
//            starttime = sdf.parse("2016-09-07 00:46:00");
//            endtime = sdf.parse("2016-09-13 14:32:00");
//            flowAnalysis.WeblogFlowAnalysis(starttime,endtime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
