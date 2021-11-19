package com.analysis.logman.read;

import com.analysis.logman.analysis.flowAnalysis.FlowForecast;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.dao.FwlogFlowDao;
import com.analysis.logman.entity.FwlogEntity;
import com.analysis.logman.entity.FwlogflowEntity;
import com.analysis.logman.entity.RawFwlog;
import com.analysis.logman.utils.AddressUtils;
import com.analysis.logman.utils.IPChangeUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 一次读取一个小时的日志存在的问题
 * 日志数量过大，产生超过内存
 */
public class ReadFwLogFile {
    public static List<RawFwlog> rawFwlogs = new LinkedList<>();
    private List<FwlogEntity> fwlogEntities = new LinkedList<>();
    private DefaultDao defaultDao = new DefaultDao();
    private FlowForecast flowForecast = new FlowForecast();
    private FwlogFlowDao fwlogFlowDao = new FwlogFlowDao();
    private IPChangeUtil ipChangeUtil = new IPChangeUtil();
    private AddressUtils addressUtils = new AddressUtils();
    //保存在list中的日志的上限值
    private int MAX_FWLOG_SIZE = 1000000;
    //    统计流量时间阈值
    private int TIME_INTERVAL = 60 * 1000;
    private String startTime = null;
    private String st = null;
    private String endTime = null;
    //    统计流量次数
    private int flow = 0;
    //    时间戳的样式
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void readFile(File file) throws IOException,ParseException {
        BufferedReader reader = null;
        try {
            //to read the chinese
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
            FwlogflowEntity fwlogflowEntity = new FwlogflowEntity();
            reader = new BufferedReader(read);//读文件
            String tempString = null;
//            根据现有数据给其赋值
            int flag=0;

            //has next line
            while ((tempString = reader.readLine())!=null) {

                RawFwlog rawFwlog = new RawFwlogRegex().ParseRawLog(tempString);
//                System.out.println(rawFwlog);
                //对于日志进行判断,即进行数据清洗
                //删除日志中未通过防火墙的数据以及server->outside的数据内容
                boolean cleanData = ((rawFwlog.getAction()!=null&&rawFwlog.getAction() != 0) || (rawFwlog.getSafetydomain()!=null&&rawFwlog.getSafetydomain() != 0));
                if(flag==0){
                    startTime = sdf.format(rawFwlog.getProducetime());
                    st = startTime;
                    flag=1;
                }
                if (cleanData){
                    //获取各个有用的字段
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
//                    String address = addressUtils.getAddresses("ip=" + ipChangeUtil.ipLongToStr(srcIp), "utf-8");
//
//                    if (address == null || address.equals("内网IP")) {
//                        address = "杭州";
//                    } else if (address.equals("XX")) {
//                        address = "域外";
//                    }
//                    fwlog.setAddress(address);
                    fwlogEntities.add(fwlog);
                    boolean timeFlag = (fwlog.getProducetime().getTime() - sdf.parse(startTime).getTime()) >= TIME_INTERVAL;
//                    统计每分钟通过防火墙的流量
                    if (timeFlag){
                        endTime = sdf.format(sdf.parse(startTime).getTime() + TIME_INTERVAL);
                        //更新这分钟的实际值
                        if (sdf.parse(endTime).getTime() - sdf.parse(st).getTime() <= 10 * TIME_INTERVAL){
                            fwlogflowEntity.setCurstream(flow);
                            fwlogflowEntity.setStarttime(sdf.parse(startTime));
                            fwlogflowEntity.setEndtime(sdf.parse(endTime));
//                            fwlogflowEntity.setUpstream(25312);
//                            fwlogflowEntity.setDownstream(21312);
                            //-1：待确定；0：正常；1：异常
                            fwlogflowEntity.setAbnormal(-1);
                            defaultDao.insert(fwlogflowEntity);
                        }else{
                            fwlogFlowDao.update(flow,sdf.parse(startTime));
                            //插入下一分钟的统计值
//                            List<Double> result = flowForecast.getFwallFlow(sdf.parse(startTime));
                            fwlogflowEntity.setStarttime(sdf.parse(endTime));
                            fwlogflowEntity.setEndtime(sdf.parse(sdf.format(sdf.parse(endTime).getTime() + TIME_INTERVAL)));
//                            fwlogflowEntity.setUpstream((int)Math.abs(result.get(0)));
//                            fwlogflowEntity.setDownstream((int)Math.round(result.get(1)));
                            fwlogflowEntity.setAbnormal(-1);
                            defaultDao.insert(fwlogflowEntity);
                        }
                        startTime = endTime;
                        flow = 0;
                    }else  {
                        flow ++;
                    }
                    if (fwlogEntities.size() > MAX_FWLOG_SIZE){
                        for (FwlogEntity fwlogEntity:fwlogEntities){
                            defaultDao.insert(fwlogEntity);
                        }
                        fwlogEntities.clear();
                    }
                }
            }
            //            文件末尾时，将所有的list中的所有数据都保存
            if ((tempString = reader.readLine()) == null){
                fwlogFlowDao.update(flow,sdf.parse(startTime));
                startTime = endTime;
                flow = 0;
                for (FwlogEntity fwlogEntity:fwlogEntities){
                    defaultDao.insert(fwlogEntity);
                }
                fwlogEntities.clear();
            }
//            System.out.println(rawFwlogs.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        try{
            new ReadFwLogFile().readFile(new File("E:\\log\\data\\20160801.02.(10.136.14.155).log(1)\\20160801.02.(10.136.14.155).log"));
//            new ReadFwLogFile().readFile(new File("E://log//log1.log"));

        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e1){
            e1.printStackTrace();
        }
    }
}
