package com.analysis.logman.read;

import com.analysis.logman.analysis.flowAnalysis.FlowForecast;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.FwlogFlowDao;
import com.analysis.logman.dao.WebFlowDao;
import com.analysis.logman.entity.*;
import com.analysis.logman.utils.IPChangeUtil;
import com.analysis.logman.utils.KeepTwoDecimalUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 监控日志文件的大小
 */
public class Mointor {
    private long fileLen;//保存之前的文件的长度
    private long newFileLen;//保存现在的文件的长度
    private RandomAccessFile raf = null;
    private List<FwlogEntity> fwlogEntities = new LinkedList<>();
    private List<WeblogEntity> weblogEntities = new LinkedList<>();
    private long fwallFlow;//统计防火墙的流量
    private int webFlow;//统计web服务器的流量
    private String mediaFileRegex = "\\w*\\.[gif|swf|jpg|JPG|midi|mp3|avi|js|css]";
    private DefaultDao defaultDao = new DefaultDao();
    private FwlogFlowDao fwlogFlowDao = new FwlogFlowDao();
    private WebFlowDao webFlowDao = new WebFlowDao();
    private String startTime;
    private String endTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FlowForecast flowForecast = new FlowForecast();
    /**
     * 监控防火墙日志的大小
     * 流量监测
     * @param fileName
     */
    public void mointorFwlogFile(String fileName) throws ParseException{
        File file = new File(fileName);
        fileLen = file.length();
        startTime = "2016-02-19 00:00:00";
        new Thread(()->{
            while(true){
                //            线程休眠1分钟
                try{
                    TimeUnit.SECONDS.sleep(60);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
//                获取现在的日志大小
                newFileLen = file.length();

                if (newFileLen > fileLen){
                    try{
                        raf = new RandomAccessFile(file,"r");
                        raf.seek(fileLen);//文件读的指针指到末尾
//                        读取文件中的所有数值
                        while(true){
                            String line = raf.readLine();
                            fwallFlow ++;//统计防火墙的访问次数
                            if (line != null && !line.equals("")){
                                line = new String(line.getBytes("ISO-8859-1"),"UTF-8");//防止乱码出现
                                RawFwlog rawFwlog = new RawFwlogRegex().ParseRawLog(line);
//                                数据清洗条件
                                boolean flag = rawFwlog.getAction() != 0 && rawFwlog.getSafetydomain() != 0;
                                if (flag){
                                    Long internalIp = new IPChangeUtil().ipStrToLong(rawFwlog.getInternalip());
                                    Date time = rawFwlog.getProducetime();
                                    String mathedStrategy = rawFwlog.getMathedstrategy();
                                    String accessStrategy = rawFwlog.getAccessstrategy();
                                    Long srcIp = rawFwlog.getOriginalsrcip();
                                    Integer srcPort = rawFwlog.getOriginalsrcport();
                                    Long destIp = rawFwlog.getOriginaldestip();
                                    Integer destPort = rawFwlog.getOriginaldestport();
                                    Integer protocol = rawFwlog.getProtocolnumber();
                                    FwlogEntity fwlog = new FwlogEntity();
                                    fwlog.setInternalip(internalIp);
                                    fwlog.setProducetime(time);
                                    fwlog.setMathedstrategy(mathedStrategy);
                                    fwlog.setAccessstrategy(accessStrategy);
                                    fwlog.setSrcip(srcIp);
                                    fwlog.setSrcport(srcPort);
                                    fwlog.setDestip(destIp);
                                    fwlog.setDestport(destPort);
                                    fwlog.setProtocol(protocol);
                                    fwlogEntities.add(fwlog);
                                }
                            }
                            //插入原始日志
                            for (FwlogEntity fwlogEntity:fwlogEntities){
                                defaultDao.insert(fwlogEntity);
                            }
                            //清空
                            fwlogEntities.clear();
                        }
                    }catch (FileNotFoundException e){
                        System.out.println("文件没有找到");
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    finally {
                        try{
                            if (raf != null)
                                raf.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                try{
                    fileLen = newFileLen;
                    endTime = sdf.format(sdf.parse(startTime).getTime() + 60 * 1000);
                    fwlogFlowDao.update((int)fwallFlow,sdf.parse(startTime));
                    //更新之后流量清零
                    fwallFlow = 0;
                    FwlogflowEntity fwlogflowEntity = new FwlogflowEntity();
                    List<Double> flows = flowForecast.getFwallFlow(sdf.parse(startTime));
                    fwlogflowEntity.setStarttime(sdf.parse(startTime));
                    fwlogflowEntity.setEndtime(sdf.parse(endTime));
                    fwlogflowEntity.setUpstream((int)Math.round(flows.get(0)));
                    fwlogflowEntity.setDownstream((int)Math.round(flows.get(1)));
                    defaultDao.insert(fwlogflowEntity);
                    startTime = endTime;

                }catch (ParseException e){
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 监控web日志文件的大小
     * 流量监测
     * @param fileName
     */
    public void mointorWebLogFile(String fileName){
        File file = new File(fileName);

        fileLen = file.length();
        new Thread(()->{
            while(true){
                try{
//                    线程休眠
                    TimeUnit.SECONDS.sleep(60);
                    newFileLen = file.length();
                    if (newFileLen > fileLen){
                        raf = new RandomAccessFile(file,"r");
                        raf.seek(fileLen);
                        String line = raf.readLine();
                        if (line != null && !line.equals("")){
                            WeblogEntity weblogEntity = new WebRegex().webRegex(line);
                            webFlow += weblogEntity.getTimes();
                            if (weblogEntity.getUrl() != null){
                                Pattern pattern = Pattern.compile(mediaFileRegex);
                                Matcher matcher = pattern.matcher(weblogEntity.getUrl());
//                                数据清洗，删除媒体文件与浏览器返回码大于400的
                                if (weblogEntity.getReactnum() < 400 && !matcher.find()){
                                    weblogEntities.add(weblogEntity);
                                }
                            }
                        }
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (FileNotFoundException e){
                    System.out.println("文件没有找到");
                }catch (IOException e){
                    e.printStackTrace();
                }
                finally {
                    try{
                        if (raf != null){
                            raf.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
//                更新文件的长度
                try{
                    fileLen = newFileLen;
                    WebflowavgEntity webflowavgEntity = webFlowDao.getLastData();
                    //插入新的事务分钟均数
                    if (sdf.parse(startTime).getTime() > webflowavgEntity.getEndtime().getTime()){
                        List<WeblogflowEntity> weblogflowEntities = webFlowDao.getFlow();
                        Date st = weblogflowEntities.get(weblogflowEntities.size() - 1).getStarttime();
                        Date et = weblogflowEntities.get(0).getEndtime();
                        double avg = 0,sum = 0;
                        WebflowavgEntity webflowavgEntity1 = new WebflowavgEntity();
                        for (int i = 0 ; i < weblogflowEntities.size() ; i++){
                            sum += weblogflowEntities.get(i).getCurstream();
                        }
                        webflowavgEntity.setStarttime(st);
                        webflowavgEntity.setEndtime(et);
                        webflowavgEntity.setAvgflow((int)new KeepTwoDecimalUtil().keepTwoDecimalPlaces(sum / weblogflowEntities.size()));
                    }
                    webFlowDao.update(webFlow,sdf.parse(startTime));
                    webFlow = 0;
                    endTime = sdf.format(sdf.parse(startTime).getTime() + 60 * 1000);
                    WeblogflowEntity weblogflowEntity = new WeblogflowEntity();
                    List<Double> lists = flowForecast.getWebFlow(sdf.parse(startTime));
                    weblogflowEntity.setStarttime(sdf.parse(startTime));
                    weblogflowEntity.setEndtime(sdf.parse(endTime));
                    weblogflowEntity.setUpstream((int)Math.round(lists.get(0)));
                    weblogflowEntity.setDownstream((int)Math.round(lists.get(1)));
                    weblogflowEntity.setAbnormal(-1);
                    defaultDao.insert(weblogflowEntity);
                }catch (ParseException e){
                    e.printStackTrace();
                }

            }
        });
    }
}
