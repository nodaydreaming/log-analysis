package com.analysis.logman.read;

import com.analysis.logman.entity.WeblogEntity;
import com.analysis.logman.utils.IPChangeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//读取web日志的正则表达式
public class WebLogRegex {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss");
    public WeblogEntity WeblogPrase(String weblog){
        WeblogEntity weblogEntity = new WeblogEntity();
//        从weblog当中取出ip
        String regexIP = "\\d+\\.\\d+\\.\\d+\\.\\d";
        Pattern p = Pattern.compile(regexIP);
        Matcher matcher = p.matcher(weblog);
        if (matcher.find()){
            String ipStr = matcher.group(0);
            Long ipLong = new IPChangeUtil().ipStrToLong(ipStr);
            weblogEntity.setSrcip(ipLong);
        }


//         从日志当中取出时间戳
        String timeRegex = "\\d+/\\w+/\\d+:\\d+:\\d+:\\d+";
        p = Pattern.compile(timeRegex);
        matcher = p.matcher(weblog);
        if (matcher.find()){
            String timestamp = matcher.group();
            weblogEntity.setProducetime(timeFormat(timestamp));
        }

//        获取方法
        String methodRegex[] = "(?i)GET|HEAD|POST".split("\\|");
        for (int i = 0 ; i < methodRegex.length ; i++){
            p = Pattern.compile(methodRegex[i]);
            matcher = p.matcher(weblog);
            if (matcher.find()){
                String method = matcher.group();
                weblogEntity.setMethod(method);
                break;
            }
        }

//        获取url
        String urlRegex = "\\s((\\\\)\\S+)*(/\\S+)+\\.*\\S+\\s";
        p = Pattern.compile(urlRegex);
        matcher = p.matcher(weblog);
        if (matcher.find()){
            String url = matcher.group();
            weblogEntity.setUrl(url);
        }

//       获取返回码
        String numRegex = "\"\\s(\\d{3})\\s(\\d+)\\s";
        p = Pattern.compile(numRegex);
        matcher = p.matcher(weblog);
        if (matcher.find()){
            //返回状态码
            String reactNum = matcher.group(1);
            //流量大小
            String flowNum = matcher.group(2);
//            System.out.println(reactNum + " " + flowNum);
            weblogEntity.setReactnum(Integer.parseInt(reactNum));
            weblogEntity.setTimes(Integer.parseInt(flowNum));
        }


//        获取浏览器
        String browserRegex = "\"\\s\"(\\S+/\\S+\\s.*\\(.+\\)*)+.*\"";
        p = Pattern.compile(browserRegex);
        matcher = p.matcher(weblog);
        if (matcher.find()){
            String browserStr = matcher.group().substring(3,matcher.group().length() - 1);
            weblogEntity.setBrowser(browserStr);
        }
        return weblogEntity;
    }
    //将日志中的时间格式转化成时间戳的样式
    public String timeFormat(String time){
        String dateTime = null;
        String mm = null;
        String[] tempTime = time.split("/");
        switch (tempTime[1]){
            case "Jan":
                mm = "01";
                break;
            case "Feb":
                mm = "02";
                break;
            case "Mar":
                mm = "03";
                break;
            case "Apr":
                mm = "04";
                break;
            case "May":
                mm = "05";
                break;
            case "Jun":
                mm = "06";
                break;
            case "Jul":
                mm = "07";
                break;
            case "Aug":
                mm = "08";
                break;
            case "Sep":
                mm = "09";
                break;
            case "Oct":
                mm = "10";
                break;
            case "Nov":
                mm = "11";
                break;
            case "Dec":
                mm = "12";
                break;
            default:
                mm = "0";
        }
        String[] timpStamp2 = tempTime[2].split(":");
        dateTime = timpStamp2[0] + "-" + mm + "-" + tempTime[0] + " " + timpStamp2[1] + ":" + timpStamp2[2] + ":" + timpStamp2[3];
        return dateTime;
    }

    public static void main(String[] args) throws ParseException{
        String webLog = "202.107.201.12 - - [06/Sep/2016:21:45:53 +0800] \"GET /html/main/F5.html HTTP/1.0\" 200 472 \"-\" \"-\"";
        System.out.println(new WebLogRegex().WeblogPrase(webLog));
    }
}
