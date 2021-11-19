package com.analysis.logman.analysis.associationanalysis;

import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.entity.AssociationeventsEntity;
import com.analysis.logman.entity.FwlogeventsEntity;
import com.analysis.logman.entity.WeblogeventsEntity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AssociationBySrcip {
    private DefaultDao defaultDao = new DefaultDao();
    private AnalysisDao analysisDao = new AnalysisDao();
    private SimpleDateFormat sdf = new SimpleDateFormat();

    private static String joinList(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String ele : list) {
            sb.append(ele);
            sb.append(",");
        }
        //把最后一个逗号去掉
        return sb.substring(0, sb.length() - 1);
    }
    //关联分析事件准备
    public void AssociationPrepared(Date starttime,Date endtime){
        //根据时间段获得该事件内防火墙和web服务器的所有异常事件
        List<FwlogeventsEntity> fwlogEventsList = analysisDao.findEventsBytime("FwlogeventsEntity",starttime,endtime);
        List<WeblogeventsEntity> weblogEventsList = analysisDao.findEventsBytime("WeblogeventsEntity",starttime,endtime);
        //根据源IP对日志事件进行分类
        HashMap<Long,List<FwlogeventsEntity>> fwlogMap = new HashMap<>();
        HashMap<Long,List<WeblogeventsEntity>> weblogMap = new HashMap<>();
        //遍历每个防火墙日志事件，进行分类
        for(FwlogeventsEntity f : fwlogEventsList){
            if(fwlogMap.keySet().contains(f.getSrcip()))
                fwlogMap.get(f.getSrcip()).add(f);
            else {
                List<FwlogeventsEntity> fwlogeventsEntities = new LinkedList<>();
                fwlogeventsEntities.add(f);
                fwlogMap.put(f.getSrcip(), fwlogeventsEntities);
            }
        }
        //遍历每个web日志事件，进行分类
        for(WeblogeventsEntity w : weblogEventsList){
            if(weblogMap.keySet().contains(w.getSrcip()))
                weblogMap.get(w.getSrcip()).add(w);
            else {
                List<WeblogeventsEntity> weblogeventsEntities = new LinkedList<>();
                weblogeventsEntities.add(w);
                weblogMap.put(w.getSrcip(), weblogeventsEntities);
            }
        }
        //寻找两种日志事件中源IP相同的事件合成一个关联分析的准备事件
        for(Long srcip : fwlogMap.keySet()){
            if(weblogMap.keySet().contains(srcip)){
                AssociationeventsEntity associationeventsEntity = new AssociationeventsEntity();
                List<FwlogeventsEntity> fList = fwlogMap.get(srcip);
                List<WeblogeventsEntity> wList = weblogMap.get(srcip);
                StringBuffer fwlogevents = new StringBuffer();
                StringBuffer weblogevents = new StringBuffer();
                //获得防火墙日志事件的集合，以字符串存储，以“,”为间隔
                for(FwlogeventsEntity f : fList) {
                    fwlogevents.append(f.getType()+",");
                }
                //获得web日志事件的集合，以字符串存储，以“,”为间隔
                int flag = 0;
                for(WeblogeventsEntity w : wList) {
                    if(flag == 0) {
                        flag = 1;
                        weblogevents.append(w.getType());
                    }
                    else
                        weblogevents.append(","+w.getType());
                }
                //将事件集合存储起来，作为关联分析的输入
                associationeventsEntity.setSrcip(srcip);
                associationeventsEntity.setStarttime(starttime);
                associationeventsEntity.setEndtime(endtime);
                associationeventsEntity.setFwlogevents(fwlogevents.toString());
                associationeventsEntity.setWebevents(weblogevents.toString());
                defaultDao.insert(associationeventsEntity);
            }
        }
    }
    //开始关联分析
    public void AssociationAnalysis(Date starttime,Date endtime,double confident,int support){
        List<AssociationeventsEntity> list = analysisDao.findAllByTime("AssociationeventsEntity",starttime,endtime);
        List<String> input = new LinkedList<>();

        FPTree fpTree = new FPTree();
        //设置置信度
        fpTree.setConfident(confident);
        //设置最小支持度
        fpTree.setMinSuport(support);
        //获得输入事务
        for(AssociationeventsEntity a : list){
            StringBuffer str = new StringBuffer();
            str.append(a.getFwlogevents());
            str.append(a.getWebevents());
            input.add(str.toString());
        }
        //List<List<String>> trans = fpTree.readTransRecords(new String[] { infile });
        List<List<String>> trans = fpTree.readTransRecords(input);
        Set<String> decideAttr = new HashSet<String>();
        //设置关联目标
        decideAttr.add("");
        //decideAttr.add("");
        fpTree.setDecideAttr(decideAttr);
        long begin = System.currentTimeMillis();
        fpTree.buildFPTree(trans);
        long end = System.currentTimeMillis();
        System.out.println("buildFPTree use time " + (end - begin));
        Map<List<String>, Integer> patterns = fpTree.getFrequentItems();

        System.out.println("模式\t频数");
        for (Map.Entry<List<String>, Integer> entry : patterns.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
            System.out.println(joinList(entry.getKey()) + "\t" + entry.getValue());
        }
        System.out.println();
        List<StrongAssociationRule> rules = fpTree.getAssociateRule();

        System.out.println("条件\t结果\t支持度\t置信度");

        DecimalFormat dfm = new DecimalFormat("#.##");
        for (StrongAssociationRule rule : rules) {
            System.out.println(rule.condition + "->" + rule.result + "\t" + dfm.format(rule.support)
                    + "\t" + dfm.format(rule.confidence));
        }
    }

    public static void main(String[] args){
        AssociationBySrcip associationBySrcip = new AssociationBySrcip();
        Date starttime = null;
        Date endtime = null;

        associationBySrcip.AssociationAnalysis(starttime,endtime,0.6,3);
    }
}
