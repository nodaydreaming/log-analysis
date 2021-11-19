package com.analysis.logman.read;

import com.analysis.logman.analysis.flowAnalysis.FlowForecast;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.WebFlowDao;
import com.analysis.logman.entity.WeblogEntity;
import com.analysis.logman.entity.WeblogflowEntity;
import com.analysis.logman.utils.KeepTwoDecimalUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从web日志当中读取日志信息，将日志信息保存到数据库当中
 * 对于url为空的数据，统计其访问的流量大小
 */

public class ReadWebLog {

    private String mediaFileRegex = "\\w*\\.[gif|swf|jpg|JPG|midi|mp3|avi|js|css]";
    //统计web服务器的流量的时间间隔
    private int TIME_INTERVAL =  60 * 1000;
    private String st = null;
    private String et = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private KeepTwoDecimalUtil keepTwoDecimalUtil = new KeepTwoDecimalUtil();
    private DefaultDao defaultDao = new DefaultDao();
    private FlowForecast flowForecast = new FlowForecast();
    private WebFlowDao webFlowDao = new WebFlowDao();

    public List<WeblogEntity> webLogRead(File file) throws IOException,ParseException{
        BufferedReader reader = null;
        List<WeblogEntity> weblogEntities = new LinkedList<>();
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "gbk");
            reader = new BufferedReader(read);
            String timeString = null;
//            读取下一行的日志内容
            //18/Aug/2016:16:15:36
            st = null;
            String st1 = null;
//            统计十五分钟的流量的平均值，设定初始时间和保存流量的list
            int flow = 0,flag = 0;
            while ((timeString = reader.readLine()) != null){
                WeblogEntity weblogEntity = new WebLogRegex().WeblogPrase(timeString);
//                统计单位时间的流量
                if(flag == 0){
                    flag = 1;
                    st = st1 = weblogEntity.getProducetime();
                }
                et = weblogEntity.getProducetime();
                //统计十五分钟的流量均值
//                boolean fifteenMinsFlag = sdf.parse(et1).getTime() - sdf.parse(average_st).getTime() >= 15 * TIME_INTERVAL;
//                if (!fifteenMinsFlag){
//                    average_sum += weblogEntity.getFlow();
//                }else{
//                    WebflowavgEntity webflowavgEntity = new WebflowavgEntity();
//                    webflowavgEntity.setSt(sdf.parse(average_st));
//                    webflowavgEntity.setEt(sdf.parse(sdf.format(sdf.parse(average_st).getTime() + 15 * TIME_INTERVAL)));
//                    webflowavgEntity.setAvgflow(keepTwoDecimalUtil.keepTwoDecimalPlaces(average_sum / 15));
//                    defaultDao.insert(webflowavgEntity);
//                    average_st = sdf.format(sdf.parse(average_st).getTime() + 15 * TIME_INTERVAL);
////                    统计结束后归零
//                    average_sum = 0;
//                }

//                时间差
                boolean timeIntervalFlag = sdf.parse(et).getTime() - sdf.parse(st).getTime() > TIME_INTERVAL;
                if (!timeIntervalFlag){
                    flow += weblogEntity.getTimes();
                }else{
                    if (sdf.parse(et).getTime() - sdf.parse(st1).getTime() <= 11 * TIME_INTERVAL){
                        WeblogflowEntity weblogflowEntity = new WeblogflowEntity();
                        et = sdf.format(sdf.parse(st).getTime() + TIME_INTERVAL);
                        weblogflowEntity.setStarttime(sdf.parse(st));
                        weblogflowEntity.setEndtime(sdf.parse(et));
                        weblogflowEntity.setCurstream(flow);
//                        weblogflowEntity.setUpstream(5400);
//                        weblogflowEntity.setDownstream(4900);
                        defaultDao.insert(weblogflowEntity);
                    }
                    else{
//                        更新实时的流量
                        webFlowDao.update(flow,sdf.parse(st));
//                        预测下一秒的流量
                        WeblogflowEntity weblogflowEntity = new WeblogflowEntity();
                        et = sdf.format(sdf.parse(st).getTime() + TIME_INTERVAL);
//                        List<Double> result = flowForecast.getWebFlow(sdf.parse(st));
                        weblogflowEntity.setEndtime(sdf.parse(sdf.format(sdf.parse(et).getTime() + TIME_INTERVAL)));
                        weblogflowEntity.setStarttime(sdf.parse(et));
                        //流量预测模块

//                        weblogflowEntity.setUpstream((int)Math.round(result.get(0)));
//                        if (result.get(1) < 0){
//                            weblogflowEntity.setDownstream((int)Math.round(result.get(0) * 0.1));
//                        }else{
//                            weblogflowEntity.setDownstream((int)Math.round(result.get(1)));
//                        }
                        defaultDao.insert(weblogflowEntity);
//                        result.clear();
                    }
                    st = et;
                    flow = 0;
                }

//                根据调研可得，当返回的状态码大于400之后，均为错误的访问信息
//                访问多媒体文件以及状态返回码为错误时，我们认为这是正常的访问，不保存到数据库中
                if (weblogEntity.getUrl() != null) {
                    Pattern p = Pattern.compile(mediaFileRegex);
                    Matcher m = p.matcher(weblogEntity.getUrl());
                    if ((weblogEntity.getReactnum() < 400) && (!m.find())) {
                        weblogEntities.add(weblogEntity);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return weblogEntities;
    }

    public static void main(String[] args) {
        try{
            new ReadWebLog().webLogRead(new File("logFile/weblog.log"));
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }

    }
}
