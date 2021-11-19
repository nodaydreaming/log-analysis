package com.analysis.logman.read;


import com.analysis.logman.entity.RawFwlog;
import com.analysis.logman.utils.IPChangeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作：匹配字符串中的数据，赋予每个字段意义
 * 并且将ip地址由String转换为long类型
 */
public class RawFwlogRegex {
    public RawFwlog ParseRawLog(String log){
        RawFwlog rawfwlog = new RawFwlog();

        //第一个时间戳的表达式
        String FirstTimeStampRegex = "\\p{Upper}\\p{Lower}{2} (\\d{2} | \\d )(\\d{2}:){2}\\d{2}";
        Pattern p = Pattern.compile(FirstTimeStampRegex);
        Matcher m = p.matcher(log);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMM dd HH:mm:ss", Locale.ENGLISH);
        if (m.find()) {
            try {
                Calendar a = Calendar.getInstance();
                rawfwlog.setSavetime(sdf.parse(a.get(Calendar.YEAR) + m.group(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //regex正则表达式
        //内网IP
        String internalIPRegex = "(\\d{1,3}\\.){3}\\d{1,3}";
        p = Pattern.compile(internalIPRegex);
        m = p.matcher(log);
        if (m.find()) {
            rawfwlog.setInternalip(m.group(0));
        }

        //时间戳正则表达式
        String timeStampRegex = "\\d{4}(-\\d{2}){2}: (\\d{2}:){2}\\d{2}";
        p = Pattern.compile(timeStampRegex);
        m = p.matcher(log);
        if (m.find()) {
            sdf = new SimpleDateFormat("yyyy-MM-dd: HH:mm:ss");
            String timeStamp1 = m.group(0);
            try {
                rawfwlog.setProducetime(sdf.parse(timeStamp1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String mathedStrategyRegex = "FW_internetDMZ_Main.+. 匹配到访问策略";
        String mathedStrategyRegex1 = "FW_internet_Main.+. 匹配到访问策略";
        p = Pattern.compile(mathedStrategyRegex);
        m = p.matcher(log);

        if (m.find()) {
            rawfwlog.setMathedstrategy(m.group(0).substring(0, m.group(0).length() - 10));
        }else{
            p = Pattern.compile(mathedStrategyRegex1);
            m = p.matcher(log);
            if (m.find()){
                rawfwlog.setMathedstrategy(m.group(0).substring(0, m.group(0).length() - 10));
            }
        }


        String accessStrategyRegex = "匹配到访问策略.+， 原始地址";
        p = Pattern.compile(accessStrategyRegex);
        m = p.matcher(log);
        if (m.find()){
            rawfwlog.setAccessstrategy(m.group(0).substring(0,m.group(0).length() - 6));
        }
        //源IP和端口
        String sourceIpAndPortRegex = "(\\d{1,3}\\.){3}\\d{1,3}\\(\\d+\\)->";
        p = Pattern.compile(sourceIpAndPortRegex);
        m = p.matcher(log);
        if (m.find()) {
            String IpAndPort = m.group(0);
            rawfwlog.setOriginalsrcip(new IPChangeUtil().ipStrToLong(IpAndPort.substring(0, IpAndPort.indexOf("("))));
            rawfwlog.setOriginalsrcport(Integer.parseInt(IpAndPort.substring(IpAndPort.indexOf("(") + 1, IpAndPort.length() - 3)));

            IpAndPort = m.group(0);
            rawfwlog.setConvertedsrcip(new IPChangeUtil().ipStrToLong(IpAndPort.substring(0, IpAndPort.indexOf("("))));
            rawfwlog.setConvertedsrcport(Integer.parseInt(IpAndPort.substring(IpAndPort.indexOf("(") + 1, IpAndPort.length() - 3)));
        }

        //目标IP和PORT
        String DestIpAndPortRegex = "->(\\d{1,3}\\.){3}\\d{1,3}\\(\\d+\\)";
        p = Pattern.compile(DestIpAndPortRegex);
        m = p.matcher(log);
        if (m.find()) {
            String IpAndPort1 = m.group(0);
            rawfwlog.setOriginaldestip(new IPChangeUtil().ipStrToLong(IpAndPort1.substring(2, IpAndPort1.indexOf("("))));
            rawfwlog.setOriginaldestport(Integer.parseInt(IpAndPort1.substring(IpAndPort1.indexOf("(") + 1, IpAndPort1.length() - 1)));

            IpAndPort1 = m.group(0);
            rawfwlog.setConverteddestip(new IPChangeUtil().ipStrToLong(IpAndPort1.substring(2, IpAndPort1.indexOf("("))));
            rawfwlog.setConverteddestport(Integer.parseInt(IpAndPort1.substring(IpAndPort1.indexOf("(") + 1, IpAndPort1.length() - 1)));
        }

        //协议 protocol
        String protocalRegex = "协议：\\d{1,3}，";
        p = Pattern.compile(protocalRegex);
        m = p.matcher(log);
        if (m.find()) {
//            String--->int
            rawfwlog.setProtocolnumber(Integer.parseInt(m.group(0).substring(3, m.group(0).length() - 1)));
        }

        //安全域 safety domainp
        String safetyDomainRegex = "安全域：.+，";
        p = Pattern.compile(safetyDomainRegex);
        m = p.matcher(log);
        if (m.find()) {
            if (m.group().substring(4,m.group().length() - 1).equals("outside->server"))
                rawfwlog.setSafetydomain(1);
            else
                rawfwlog.setSafetydomain(0);
        }

        //动作 action
        p = Pattern.compile("动作：\\D{2}。");
        m = p.matcher(log);
        while (m.find()) {
            if (m.group().substring(3, m.group().length() - 1).equals("允许")) {
                rawfwlog.setAction(1);
            } else {
                rawfwlog.setAction(0);
            }
        }
        return rawfwlog;
    }

    public static void main(String[] args) {
//        String log = "Aug  1 00:00:00 11.116.14.155 2016-07-31: 23:42:39 FW_internetDMZ_Main:root 03-011-069-0017 Notice Session N/A rep=1 | 匹配到访问策略FOR_SERVER， 原始地址：172.21.181.82(58073)->212.117.201.1(53)， 协议：17，  转换后地址：172.22.181.82(58173)->202.127.211.1(53)， 安全域：outside->server， 动作：允许。";
        String log = "Feb 19 00:00:00 10.136.14.133 2016-02-19: 00:00:00 FW_internet_Main:root 03-01-069-0017 Notice Session N/A rep=1 | 匹配到访问策略in_out， 原始地址：172.22.144.29(3330)->203.195.143.237(80)， 协议：6，  转换后地址：172.22.144.29(3330)->203.195.143.237(80)， 安全域：inside->outside， 动作：允许。";
        System.out.println(new RawFwlogRegex().ParseRawLog(log));
    }
}
