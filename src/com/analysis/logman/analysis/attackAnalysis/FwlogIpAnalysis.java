package com.analysis.logman.analysis.attackAnalysis;

import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.entity.DestipanalysisEntity;
import com.analysis.logman.entity.FwlogEntity;
import com.analysis.logman.entity.FwlogeventsEntity;
import com.analysis.logman.entity.SrcipanalysisEntity;
import com.analysis.logman.utils.AddressUtils;
import com.analysis.logman.utils.IPChangeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FwlogIpAnalysis {
    private AnalysisDao analysisDao = new AnalysisDao();
    private DefaultDao defaultDao = new DefaultDao();
    private IPChangeUtil ipChangeUtil = new IPChangeUtil();
    private AddressUtils addressUtils =new AddressUtils();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /*
    *根据起止时间，得到该时间段中访问次数超过正常值的源IP按照访问次数由高到低逐一分析
    *跟据源IP，得到该IP访问的各个目的IP及对应的被访问次数
    */
    //源IP分析
    public void SrcIpScanAnalysis(Date starttime, Date endtime) {
        List<Map<Long,Integer>> maps = new ArrayList<>();
        List<Long> count = new ArrayList<>();

        try {
            //根据起止时间，得到该时间段中访问次数TOP10的源IP
            List<Long> srcipList = analysisDao.findTop10IP("FwlogEntity", "srcip", starttime, endtime);
            if(srcipList!=null) {
                //遍历这十个源IP
                for (long srcip : srcipList) {
                    //在这个时间段中，这个IP的所有访问记录
                    List<FwlogEntity> srcipFwlog = analysisDao.findBySrcIP("FwlogEntity", srcip, starttime, endtime);
                    //记录该源IP在这段时间内的访问总次数
                    count.add((long)srcipFwlog.size());
                    Map<Long,Integer> map = new HashMap<>();
                    //该源IP访问的各个目的IP及对应被访问次数
                    for (FwlogEntity f : srcipFwlog) {
                        if (map.containsKey(f.getDestip()))
                            map.put(f.getDestip(), map.get(f.getDestip()) + 1);
                        else
                            map.put(f.getDestip(), 1);
                    }
                    maps.add(map);
                }
                //结果存入数据库
                for (int i = 0; i < maps.size(); i++) {
                    //源IP分析结果存入数据库
                    SrcipanalysisEntity srcipanalysisEntity = new SrcipanalysisEntity();
                    srcipanalysisEntity.setSrcip(srcipList.get(i));
                    srcipanalysisEntity.setTotalnum(count.get(i));
                    srcipanalysisEntity.setStarttime(starttime);
                    srcipanalysisEntity.setEndtime(endtime);
                    //源IP地址溯源
                    String address = addressUtils.getAddresses("ip=" + ipChangeUtil.ipLongToStr(srcipList.get(i)), "utf-8");
                    srcipanalysisEntity.setSrcipaddress(address);

                    List<Map.Entry<Long,Integer>> entryList = new ArrayList<>(maps.get(i).entrySet());
                    Collections.sort(entryList, new Comparator<Map.Entry<Long, Integer>>() {
                        @Override
                        public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                            if(o1.getValue()<o2.getValue()){
                                return 1;
                            }
                            else if(o1.getValue()>o2.getValue()){
                                return -1;
                            }
                            else{
                                return  0;
                            }
                        }

                    });

                    //目的IP数
                    int size = maps.get(i).size();
                    //ip连接的标记
                    int flag=0;
                    int perMinTimes = 20,cnt = 0;
                    //记录各个目的IP和被访问次数
                    String destips = "";
                    String counts = "";
                    for (Map.Entry<Long,Integer> destipmap : entryList) {
                        //只保存被访问次数异常的目的IP
                        long l = destipmap.getKey();
                        int minute = (int)(endtime.getTime()-starttime.getTime())/1000/60;
                        if(maps.get(i).get(l)>perMinTimes * minute) {
                            cnt++;
                            if (flag==1) {
                                destips = destips+","+l;
                                counts = counts+","+maps.get(i).get(l);
                            }else{
                                destips = destips + l;
                                counts = counts + maps.get(i).get(l);
                                flag=1;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    entryList.clear();
                    //被源IP访问次数超过正常值的目的IP数
                    srcipanalysisEntity.setTotaldestipnum(size);
                    srcipanalysisEntity.setDestips(destips);
                    srcipanalysisEntity.setCounts(counts);
                    //根据目的IP数判定是否为IP扫描攻击
                    if (size > 200) {
                        int sum = 10000;//此防火墙内所有的IP数
                        srcipanalysisEntity.setIsattack(1);
                        FwlogeventsEntity fwlogeventsEntity = new FwlogeventsEntity();
                        fwlogeventsEntity.setStarttime(starttime);
                        fwlogeventsEntity.setEndtime(endtime);
                        fwlogeventsEntity.setType(1);
                        fwlogeventsEntity.setSrcip(srcipList.get(i));
                        String description = "从"+sdf.format(starttime)+"到"+sdf.format(endtime)
                                             +",共"+(endtime.getTime()-starttime.getTime())/1000
                                             +"秒中，该srcIP"+ipChangeUtil.ipLongToStr(srcipList.get(i))
                                             +"访问了局域网内"+size+"个IP，占总IP数的"
                                             +Math.round((double)size/(double)sum*100)+"%，疑似为IP扫描行为。";
                        fwlogeventsEntity.setDescription(description);
                        defaultDao.insert(fwlogeventsEntity);
                    } else {
                        srcipanalysisEntity.setIsattack(0);
                    }
                    if(cnt!=0)
                        defaultDao.insert(srcipanalysisEntity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //目的IP分析
    public void DestIpAnalysis(Date starttime,Date endtime){
        List<Map<Long,Integer>> srcIps = new ArrayList<>();
        List<Long> count = new ArrayList<>();
        try {
            //根据起止时间，得到该时间段中被访问次数TOP10的目的IP
            List<Long> destipList = analysisDao.findTop10IP("FwlogEntity", "destip", starttime, endtime);
            if(destipList != null){
                //遍历这十个目的IP
                for(long destIp:destipList){
                    List<FwlogEntity> list = analysisDao.findByDestIP("FwlogEntity",destIp,starttime,endtime);
                    //记录在这个时间段内该目的IP的被访问次数
                    count.add((long)list.size());
                    Map<Long,Integer> map = new HashMap<>();
                    for(FwlogEntity f:list){
                        if (map.containsKey(f.getSrcip()))
                            map.put(f.getSrcip(), map.get(f.getSrcip()) + 1);
                        else
                            map.put(f.getSrcip(), 1);
                    }
                    srcIps.add(map);
                }
                //结果存入数据库
                for (int i = 0; i < srcIps.size(); i++) {
                    DestipanalysisEntity destipanalysisEntity = new DestipanalysisEntity();
                    destipanalysisEntity.setDestip(destipList.get(i));
                    destipanalysisEntity.setStarttime(starttime);
                    destipanalysisEntity.setEndtime(endtime);

                    List<Map.Entry<Long,Integer>> entryList = new ArrayList<>(srcIps.get(i).entrySet());
                    Collections.sort(entryList, new Comparator<Map.Entry<Long, Integer>>() {
                        public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                            if(o1.getValue()<o2.getValue()){
                                return 1;
                            }
                            else if(o1.getValue()>o2.getValue()){
                                return -1;
                            }
                            else{
                                return 0;
                            }
                        }
                    });
                    //记录各个源IP及访问次数
                    String srcips = "";
                    String counts = "";

                    //该目的IP对应源IP的个数
                    int size = srcIps.get(i).size();
                    int flag = 0;
                    int perMinTimes = 20,cnt = 0;
                    for (Map.Entry<Long,Integer> srcipmap : entryList) {
                        long l = srcipmap.getKey();
                        int minute = (int)(endtime.getTime()-starttime.getTime())/1000/60;
                        if(srcIps.get(i).get(l)>perMinTimes * minute) {
                            cnt++;
                            if (flag==1) {
                                srcips = srcips+","+l;
                                counts = counts+","+srcIps.get(i).get(l);
                            }else{
                                srcips = srcips + l;
                                counts = counts + srcIps.get(i).get(l);
                                flag=1;
                            }
                        }
                    }
                    entryList.clear();
                    //该目的IP在这段时间内的被访问次数
                    destipanalysisEntity.setTotalnum(count.get(i));
                    destipanalysisEntity.setSrcips(srcips);
                    destipanalysisEntity.setCounts(counts);
                    //根据源IP数判定攻击(以及源IP的访问次数是否超过正常值)
                    if (size > 200) {
                        destipanalysisEntity.setIsattack(1);
                        FwlogeventsEntity fwlogeventsEntity = new FwlogeventsEntity();
                        fwlogeventsEntity.setStarttime(starttime);
                        fwlogeventsEntity.setEndtime(endtime);
                        fwlogeventsEntity.setType(4);
                        fwlogeventsEntity.setDestip(destipList.get(i));
                        String description = "从"+sdf.format(starttime)
                                            +"到"+sdf.format(endtime)
                                            +",共"+(endtime.getTime()-starttime.getTime())/1000
                                            +"秒中，该destIP("+ipChangeUtil.ipLongToStr(destipList.get(i))
                                            +")被"+size+"个外部IP频繁访问，疑似为DDoS攻击。";

                        fwlogeventsEntity.setDescription(description);
                        defaultDao.insert(fwlogeventsEntity);
                    } else {
                        destipanalysisEntity.setIsattack(0);
                    }
                    if(cnt!=0)
                        defaultDao.insert(destipanalysisEntity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FwlogIpAnalysis fwlogIpAnalysis = new FwlogIpAnalysis();
        AnalysisDao analysisDao = new AnalysisDao();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date starttime = null;
        Date endtime = null;
        try {
            starttime = sdf.parse("2016-02-19 00:00:00");
            endtime = sdf.parse("2016-02-19 01:00:00");
            fwlogIpAnalysis.SrcIpScanAnalysis(starttime,endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
