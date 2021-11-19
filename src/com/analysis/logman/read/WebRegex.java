package com.analysis.logman.read;

import com.analysis.logman.entity.WeblogEntity;
import com.analysis.logman.utils.IPChangeUtil;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一整个web日志的处理模式
 */
public class WebRegex {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private IPChangeUtil ipChangeUtil = new IPChangeUtil();
    public WeblogEntity webRegex(String log){
        WeblogEntity weblogEntity = new WeblogEntity();
        String weblogRegex = "(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})(\\s)(\\S+|-)(\\s)(\\S+|-)(\\s\\[)([0-9a-zA-Z\\+-:/ ]+)(\\]\\s\\\")(\\S+)(\\s)(\\S+)(\\s)(\\S+)(\\\"\\s)(\\d+)(\\s)(\\d+)(\\s\\\")(\\S+)(\\\"\\s\\\")(.+)(\\\")";

        Pattern pattern = Pattern.compile(weblogRegex);
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()){
            //ip
            for (int i = 1 ; i < 22 ; i+=2){
                if (i == 1){
                    weblogEntity.setSrcip(ipChangeUtil.ipStrToLong(matcher.group(i)));
                }
                if (i == 7){
                    String time = matcher.group(i);
                    String[] times = time.split(" ");
                    String reslut = timeFormat(times[0]);
                    weblogEntity.setProducetime(reslut);
                    System.out.println(reslut);
                }
                if (i == 9){
                    weblogEntity.setMethod(matcher.group(i));
                }
                if (i == 11){
                    weblogEntity.setUrl(matcher.group(i));
                }
                if (i == 15){
                    weblogEntity.setReactnum(Integer.parseInt(matcher.group(i)));
                }
                if (i == 17){
                    weblogEntity.setTimes(Integer.parseInt(matcher.group(i)));
                }
                if (i == 21){
                    weblogEntity.setBrowser(matcher.group(i));
                }

            }
//            System.out.println(weblogEntity);
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

    public static void main(String[] args) {
        WebRegex webRegex = new WebRegex();
        String log = "172.19.252.104 - - [09/Sep/2016:17:35:14 +0800] \"GET /scripts/bb_smilies.php?user=MToxOjE6MToxOjE6MToxOjE6Li4vLi4vLi4vLi4vLi4vZXRjL3Bhc3N3ZAAK HTTP/1.1\" 302 154 \"-\" \"Mozilla/4.75 [en] (X11, U; Rsas)\"";
        System.out.println(webRegex.webRegex(log));
    }
}
