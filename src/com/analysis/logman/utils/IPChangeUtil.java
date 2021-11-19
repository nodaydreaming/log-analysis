package com.analysis.logman.utils;

/**
 * IP的转变
 * @author jamesZhan
 * @date 2018-01-18
 */
public class IPChangeUtil {
    /**
     * ip(String)转Long
     * @return Long
     */
    public long ipStrToLong(String ipAddress){
        Long[] ip = new Long[4];
        int i = 0;
        for (String ipStr : ipAddress.split("\\.")){
            ip[i] = Long.parseLong(ipStr);
            i++;
        }
//        << 左移
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * IP Long 转 String
     * return String
     */
    public String ipLongToStr(long ipAddress){
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(ipAddress >>> 24));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x000000FF)));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new IPChangeUtil().ipStrToLong("172.31.214.196"));
//        System.out.println(new IPChangeUtil().ipLongToStr(2130706433));
        System.out.println(new IPChangeUtil().ipLongToStr(new IPChangeUtil().ipStrToLong("172.31.214.196")));
    }
}
