package com.analysis.logman.analysis.attackAnalysis;

import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.entity.*;
import com.analysis.logman.utils.AddressUtils;
import com.analysis.logman.utils.IPChangeUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class FwlogPortAnalysis {
    private AnalysisDao analysisDao = new AnalysisDao();
    private DefaultDao defaultDao = new DefaultDao();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private IPChangeUtil ipChangeUtil = new IPChangeUtil();
    private AddressUtils addressUtils = new AddressUtils();
    //根据目的IP被攻击的分析结果，进而分析端口扫描
    public void DestPortScanAnalysis(Date starttime, Date endtime) {
        List<List<Long>> srcipLists = new ArrayList<>();
        try {
            List<DestipanalysisEntity> list = analysisDao.findAllByTime("DestipanalysisEntity", starttime, endtime);
            if (list == null|| list.size()== 0) {
                FwlogIpAnalysis fwlogIpAnalysis = new FwlogIpAnalysis();
                fwlogIpAnalysis.DestIpAnalysis(starttime, endtime);
                list = analysisDao.findAllByTime("DestipanalysisEntity", starttime, endtime);
            }
            if(list!=null) {
                //取出目的IP分别对应的源IP集合
                for (DestipanalysisEntity i : list) {
                    String[] srcips = i.getSrcips().split(",");
                    List<Long> srcipList = new ArrayList<>();
                    for (int k = 0; k < srcips.length; k++) {
                        long srcip = Long.parseLong(srcips[k]);
                        srcipList.add(srcip);
                    }
                    srcipLists.add(srcipList);
                }
                //根据源IP和目的IP分析端口
                for (int i = 0; i < srcipLists.size(); ++i) {
                    long destip = list.get(i).getDestip();

                    for (long l : srcipLists.get(i)) {
                        long srcip = l;
                        List<FwlogEntity> list1 = analysisDao.findByIPAndTime("FwlogEntity", srcip, destip, starttime, endtime);
                        Map<Integer, Integer> map = new HashMap<>();
                        for (FwlogEntity f : list1) {
                            if (map.containsKey(f.getDestport())) {
                                map.put(f.getDestport(), map.get(f.getDestport()) + 1);
                            } else {
                                map.put(f.getDestport(), 1);
                            }
                        }

                        //此srcIP访问目的IP下端口的数量
                        int size = map.size();
                        //端口扫描分析
                        if (map.size() > 50) {
                            int sum = 200;
                            FwlogeventsEntity fwlogeventsEntity = new FwlogeventsEntity();
                            fwlogeventsEntity.setStarttime(starttime);
                            fwlogeventsEntity.setEndtime(endtime);
                            fwlogeventsEntity.setType(2);
                            fwlogeventsEntity.setSrcip(srcip);
                            fwlogeventsEntity.setDestip(destip);

                            String description = "从"+sdf.format(starttime)+"到"+sdf.format(endtime)
                                                +",共"+(endtime.getTime()-starttime.getTime())/1000
                                                +"秒中，该srcIP("+ipChangeUtil.ipLongToStr(srcip)
                                                +")访问destIP("+ipChangeUtil.ipLongToStr(destip)+")内"
                                                +size+"个端口，占总端口数的"
                                                +Math.round((double)size/(double)sum*100)+"%，疑似为端口扫描行为。";

                            fwlogeventsEntity.setDescription(description);
                            defaultDao.insert(fwlogeventsEntity);
                        }
                        //敏感端口被频繁访问分析
                        for (int inte : map.keySet()) {

                            //如果一个端口的被访问次数大于阈值（暂定1000），则判断是否为敏感端口
                            if (map.get(inte) > 50) {
                                List<SensitiveportEntity> list2 = analysisDao.findByPort("SensitiveportEntity", inte);
                                if (list2.size()>0) {
                                    FwlogeventsEntity fwlogeventsEntity = new FwlogeventsEntity();
                                    fwlogeventsEntity.setSrcip(srcip);
                                    fwlogeventsEntity.setDestip(destip);
                                    fwlogeventsEntity.setDestport(inte);
                                    fwlogeventsEntity.setStarttime(starttime);
                                    fwlogeventsEntity.setEndtime(endtime);
                                    fwlogeventsEntity.setType(3);
                                    String description = "从"+sdf.format(starttime)
                                                        +"到"+sdf.format(endtime)+","
                                                        +"共"+(endtime.getTime()-starttime.getTime())/1000+"秒中,"
                                                        +"该srcIP("+ipChangeUtil.ipLongToStr(srcip)
                                                        +")访问destIP("+ipChangeUtil.ipLongToStr(destip)+")下的敏感端口"+inte
                                                        +"总计"+map.get(inte)+"次。";
                                    fwlogeventsEntity.setDescription(description);
                                    defaultDao.insert(fwlogeventsEntity);
                                }
                                else{
                                    DestportanalysisEntity destportanalysisEntity = new DestportanalysisEntity();
                                    destportanalysisEntity.setSrcip(srcip);
                                    destportanalysisEntity.setDestip(destip);
                                    destportanalysisEntity.setStarttime(starttime);
                                    destportanalysisEntity.setEndtime(endtime);
                                    destportanalysisEntity.setDestports(inte+"");
                                    destportanalysisEntity.setCounts(map.get(inte)+"");
                                    destportanalysisEntity.setIsattack(1);
                                    String srcipstr = ipChangeUtil.ipLongToStr(srcip);
                                    String address = addressUtils.getAddresses("ip="+srcipstr,"utf-8");

                                    destportanalysisEntity.setSrcipaddress(address);
                                    defaultDao.insert(destportanalysisEntity);
                                }
                            }

                        }


                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        FwlogPortAnalysis fwlogPortAnalysis = new FwlogPortAnalysis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date starttime = null;
        Date endtime = null;
        try {
            starttime = sdf.parse("2016-02-19 00:00:00");
            endtime = sdf.parse("2016-02-19 01:00:00");
            fwlogPortAnalysis.DestPortScanAnalysis(starttime,endtime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
