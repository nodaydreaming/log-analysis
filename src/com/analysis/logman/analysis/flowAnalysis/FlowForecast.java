package com.analysis.logman.analysis.flowAnalysis;

import com.analysis.logman.dao.FwlogFlowDao;
import com.analysis.logman.dao.WebFlowDao;
import com.analysis.logman.entity.FwlogflowEntity;
import com.analysis.logman.entity.WeblogflowEntity;
import com.analysis.logman.utils.KeepTwoDecimalUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流量预测
 * @author jamesZhan
 * date 2018-05-03
 */
public class FlowForecast {
//    规定时间戳的样式
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int TIME_INTERVAL = 60 * 1000;//时间阈值
    private WebFlowDao webFlowDao = new WebFlowDao();
    private FwlogFlowDao fwlogFlowDao = new FwlogFlowDao();
    private long WEB_FLOW_EXTREMUM = 5192 * 5;
    private long FWLOG_FLOW_EXTREMUM = 25000 * 5;
//    保留两位小数
    private KeepTwoDecimalUtil keepTwoDecimalUtil = new KeepTwoDecimalUtil();
//    权重
    private List<Double> weights = new LinkedList<>();
//    传入需要学习的参数
    private List<Double> flowForecasr(List<Double> flow){
//        2.算出加权时序平均数
        double weighted_average = 0,sum = 0;
        double maxFlow,minFlow;
        double upFlow,downFlow;//保存的预测值
        double weight = 1 * 1.00 / flow.size();
//        设定动态权重
        for (int i = 0 ; i < flow.size() ; i++){
            weights.add(weight);
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0 ; i < weights.size() ; i++){
            weighted_average += flow.get(i) * weights.get(i);
        }
        for (int i = 0 ; i < weights.size() ; i++){
            sum += (flow.get(i) - weighted_average) * (flow.get(i) - weighted_average);
        }
        sum = sum / weights.size();

//        4.计算出预测的最值，最大值和最小值
        maxFlow = Collections.max(flow);
        minFlow = Collections.min(flow);

        upFlow = maxFlow + (weighted_average - minFlow) * Math.sqrt(sum) / weighted_average;
        downFlow = minFlow - (weighted_average - minFlow) * Math.sqrt(sum) / weighted_average;

        upFlow = keepTwoDecimalUtil.keepTwoDecimalPlaces(upFlow);
        downFlow = keepTwoDecimalUtil.keepTwoDecimalPlaces(downFlow);
//        结果保存到list中
        result.add(upFlow);
        result.add(downFlow);
        //结束之后清除内部数据
        weights.clear();
        return result;
    }

    //防火墙流量的预测
    public List<Double> getFwallFlow(Date st){
        List<Double> result = new ArrayList<>();
        //根据传递过来的数据
        List<FwlogflowEntity> fwlogflowEntities = fwlogFlowDao.getFwlogFlowByTime(st);
//        对于异常的流量数据，这里不加入考虑
        List<Double> flow = new ArrayList<>();
        for (FwlogflowEntity fwlogflowEntity : fwlogflowEntities){
//            对于流量与均值流量偏差较大时，我们认为这是异常访问引起的，不加考虑
            if (fwlogflowEntity.getCurstream() < FWLOG_FLOW_EXTREMUM){
                flow.add(fwlogflowEntity.getCurstream()*1.0);
            }
            if (flow.size() >= 10){
                result = flowForecasr(flow);
                flow.clear();
                break;
            }
        }
        return result;
    }
    //web的流量预测
    public List<Double> getWebFlow(Date st) throws ParseException{
        WeblogflowEntity weblogflowEntity = webFlowDao.getFirstData();
        List<Double> result = new LinkedList<>();
        List<Double> finalList = new LinkedList<>();
        List<WeblogflowEntity> flowEntities = webFlowDao.getInstantFlow(st);
        List<Double> instanseFlow = new LinkedList<>();
        List<Double> finalInstanceFlow = new LinkedList<>();
        double instanseAvgFlow = 0; double instanseSumFlow = 0;
        for (WeblogflowEntity flowEntity : flowEntities){
            instanseFlow.add(flowEntity.getCurstream()*1.0);
        }
        for (int i = 0 ; i < instanseFlow.size() ; i++){
            instanseSumFlow += instanseFlow.get(i);
        }
        instanseAvgFlow = instanseSumFlow / instanseFlow.size();
        for (int i = 0 ; i < instanseFlow.size() ; i++){
            if (instanseFlow.get(i) <= 10 * instanseAvgFlow){
                finalInstanceFlow.add(instanseFlow.get(i));
            }
        }
        if (finalInstanceFlow.size() >= 10){
            finalInstanceFlow = finalInstanceFlow.subList(1,10);
        }
        if (st.getTime() - weblogflowEntity.getStarttime().getTime() >= 15 * 1000){
            List<Integer> flowAvg = webFlowDao.getWeblogByTime(st);
//        删除其中的误差数值
            double sum = 0;double average = 0;
            for (int i = 0 ; i < flowAvg.size() ; i++){
                sum = sum + (double)flowAvg.get(i);
            }
            average = sum / flowAvg.size();
            ListIterator<Integer> iterator = flowAvg.listIterator();

            while(iterator.hasNext()){
                double flow = iterator.next();
                if (flow >= average * 10){
                    iterator.remove();
                }else{
                    finalList.add(flow);
                }
            }

            if (finalList.size() > 10){
                finalList = finalList.subList(1,10);
            }
        }


        finalList.addAll(finalInstanceFlow);
        result = flowForecasr(finalList);
        return result;
    }

    public static void main(String[] args) {
        FlowForecast flowForecast = new FlowForecast();
        List<Double> list = new ArrayList<>();
        list.add((double)17177);
        list.add((double)5192);
        list.add((double)4352);
        list.add((double)6192);
        list.add((double)5192);
        list.add((double)5346);
        list.add((double)5192);
        list.add((double)7192);
        list.add((double)5192);
        list.add((double)5692);

        List<Double> result = flowForecast.flowForecasr(list);
        System.out.println(result.get(0) +"    " + result.get(1));
    }
}