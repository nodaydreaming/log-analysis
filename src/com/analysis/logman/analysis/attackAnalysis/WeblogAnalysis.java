package com.analysis.logman.analysis.attackAnalysis;

import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.dao.DefaultDao;
import com.analysis.logman.entity.WeblogeventsEntity;
import com.analysis.logman.entity.WeblogstatisticsEntity;
import com.analysis.logman.utils.AddressUtils;
import com.analysis.logman.utils.IPChangeUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeblogAnalysis {
    private DefaultDao defaultDao = new DefaultDao();
    private AddressUtils addressUtils = new AddressUtils();
    private IPChangeUtil ipChangeUtil =new IPChangeUtil();
    AnalysisDao analysisDao = new AnalysisDao();
    //根据请求方式和访问的URL地址，利用攻击特征库判断攻击类型
    public int AttackDetect(String method, String url){

        int AttackType=0;
        String str3 = url;

        //对输入日志进行预判断
        int flag = 0;//0：代表根据URL判定，此条记录不是攻击行为
        String[] regex = "(?i)(\\w)\\.asp|(?i)(\\w)\\.aspx|(?i)(\\w)\\.php|(?i)(\\w)\\.jsp|(?i)(\\w)\\.do|(?i)(\\w)\\.action|(?i)(\\w)\\.txt|(?i)(\\w)\\.html|(?i)(\\w)\\.shtml|(\\w)\\;(\\w)|(\\w+)\\.(\\w+)\\.".split("\\|");
        //对URL进行分析
        for (int i = 0; i < regex.length; i++) {
            Pattern p = Pattern.compile(regex[i]);
            Matcher m = p.matcher(str3);
            if (m.find()){
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            return AttackType;
        }

//        //判断信息扫描特征
//        if (("HEAD").equals(str2)){
//            AttackType = 1;
//            return AttackType;
//        }
        if (str3.length() > 5){

            //定义Struts2远程命令执行漏洞特征
            String[] regexStruts2 = "(?i)denyMethodExecution|(?i)allowStaticMethodAccess".split("\\|");
            //定义SQL注入攻击特征
            String[] regexSQL = "(\\w+)'|(?i)(\\w+)%20and%20(\\S+)|(?i)(\\w+)%20or%20(\\S+)|(\\w+)=(\\d+)-(\\d+)|(\\d+)>(\\d+)|(\\d+)<(\\d+)|(?i)(\\S)waitfor(\\W+)delay(\\S)|(?i)(\\S)having(\\W)|(?i)(\\S)sleep(\\W)|(\\w)\\+(\\w)|(\\w)\\#|(\\w)--|(\\w)\\/\\*(\\S)|(\\w)\\&\\&(\\W)|(?i)(\\S)select(\\W)|(?i)(\\S)insert(\\S+)into(\\W)|(?i)(\\S)delete(\\W)|(?i)(\\S)update(\\W)|(?i)(\\S)create(\\W)|(?i)(\\S)drop(\\W)|(?i)(\\S)exists(\\W)|(?i)(\\S)backup(\\W)|(?i)(\\S)order(\\S+)by(\\W)|(?i)(\\S)group(\\S+)by(\\W)|(?i)(\\S)exec(\\S)|(?i)(\\S)truncate(\\S)|(?i)(\\S)declare(\\S)|(?i)(\\S)@@version(\\S)".split("\\|");
            //定义XSS攻击特征
            String[] regexXSS = "(?i)(\\S)%3C(\\S+)%3E|(?i)(\\S)%3C(\\S+)%2F%3E|(\\S+)<(\\S+)>|(\\S+)<(\\S+)\\/>|\\=javascript".split("\\|");
            //定义文件包含和路径遍历攻击特征
            String[] regexFileInclude = "(?i)\\/etc\\/passwd|(?i)\\/%c0%ae%c0%ae|(?i)\\/%2E%2E|(?i)boot\\.ini|(?i)win\\.ini|\\.\\.\\/|(?i)access\\.log|(?i)httpd\\.conf|(?i)nginx\\.conf|(?i)\\/proc\\/self\\/environ".split("\\|");
            //定义常见WebShell特征
            String[] regexWebShell = "(?i)\\/cmd\\.asp|(?i)\\/diy\\.asp|(?i)\\.asp;|(?i)\\/(\\w+)\\.(\\w+)\\/(\\w+)\\.php|(?i)\\.php\\.|(?i)eval\\(|(?i)%eval|(?i)\\.jsp?action=|(?i)fsaction=".split("\\|");
            //网站敏感文件访问
            String[] regexFileAccess = "(?i)\\/WEB-INF\\/web\\.xml|(?i)applicationContext\\.xml|(?i)\\/manager\\/html|(?i)\\/jmx-console\\/|(?i)\\.properties|(?i)\\.class|(?i)phpinfo\\.php|(?i)\\/conn\\.asp|(?i)\\/conn\\.php|(?i)\\/conn\\.jsp".split("\\|");
            //系统命令注入攻击特征
            String[] regexCommand = "(?i)(\\S+)cmd(.+)echo|(?i)(\\S+)command(\\S+)|(?i)(\\S+)cmdwebshell(\\S+)|(?i)(\\S+)exec(\\S+)|(?i)(\\S+)system(\\S+)|(?i)(\\S+)passthru(\\S+)|(?i)(\\S+)popen(\\S+)|(?i)(\\S+)shell_exec(\\S+)|(?i)(\\S+)backtick(.+)operator(\\S+)".split("\\|");
            //网络参数修改
            String[] regexRequest = "\\?(\\S+)=((\\w+)@(\\w+)\\.(\\w+)(\\S+))+|\\?(\\w+)=(\\w+)\\<(\\S+)|\\?(\\w+)=(\\w+)\\>(\\S+)|\\?(\\w+)=(\\w+)\"(\\S+)|\\?(\\w+)=(\\w+)\\'(\\S+)|\\?(\\w+)=(\\w+)\\%(\\S+)|\\?(\\w+)=(\\w+)\\;(\\S+)|\\?(\\w+)=(\\w+)\\((\\S+)|\\?(\\w+)=(\\w+)\\)(\\S+)|\\?(\\w+)=(\\w+)\\+(\\S+)".split("\\|");

            //测试Struts2远程命令执行漏洞特征
            for (int i = 0; i < regexStruts2.length; i++) {
                Pattern p = Pattern.compile(regexStruts2[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 7;
                    return AttackType;
                }
            }
            //测试SQL注入攻击特征
            for (int i = 0; i < regexSQL.length; i++) {
                Pattern p = Pattern.compile(regexSQL[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType= 8;
                    return AttackType;
                }
            }
            //测试XSS攻击特征
            for (int i = 0; i < regexXSS.length; i++) {
                Pattern p = Pattern.compile(regexXSS[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 9;
                    return AttackType;
                }
            }
            //测试文件包含和路径遍历攻击特征
            for (int i = 0; i < regexFileInclude.length; i++) {
                Pattern p = Pattern.compile(regexFileInclude[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 10;
                    return AttackType;
                }
            }
            //测试WebShell攻击特征
            for (int i = 0; i < regexWebShell.length; i++) {
                Pattern p = Pattern.compile(regexWebShell[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 11;
                    return AttackType;
                }
            }
            //测试网站敏感文件访问特征
            for (int i = 0; i < regexFileAccess.length; i++) {
                Pattern p = Pattern.compile(regexFileAccess[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 12;
                    return AttackType;
                }
            }
            //测试系统命令注入攻击特征
            for (int i = 0; i < regexCommand.length; i++) {
                Pattern p = Pattern.compile(regexCommand[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 13;
                    return AttackType;
                }
            }
            //测试网络参数修改攻击特征
            for (int i = 0; i < regexRequest.length; i++) {
                Pattern p = Pattern.compile(regexRequest[i]);
                Matcher m = p.matcher(url);
                if(m.find()){
                    AttackType = 14;
                    return AttackType;
                }
            }
            return AttackType;
        }
        return AttackType;
    }
    //Web日志攻击判定，以及攻击记录的存储
    public void WeblogAttackDetect() {

        try{
            List<WeblogstatisticsEntity> list = analysisDao.webFindAll("WeblogstatisticsEntity");
            if(list!=null) {
                //System.out.println(list.size());
                for (WeblogstatisticsEntity w : list) {

                    int type = this.AttackDetect(w.getMethod(), w.getUrl());
                    if (type != 0) {
                        WeblogeventsEntity weblogeventsEntity = new WeblogeventsEntity();
                        weblogeventsEntity.setType(type);
                        weblogeventsEntity.setStarttime(w.getStarttime());
                        weblogeventsEntity.setEndtime(w.getEndtime());
                        weblogeventsEntity.setSrcip(w.getSrcip());
                        weblogeventsEntity.setWeblogid(w.getId());
                        //源IP地址溯源
                        String address = addressUtils.getAddresses("ip=" + ipChangeUtil.ipLongToStr(w.getSrcip()), "utf-8");
                        String utf8address;
                        utf8address = new String(address.getBytes(),"utf-8");
                        weblogeventsEntity.setAttackaddress(address);
                        defaultDao.insert(weblogeventsEntity);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        analysisDao.setAnalysis();
    }

    public static void main(String[] args) {
        WeblogAnalysis weblogAnalysis = new WeblogAnalysis();
        weblogAnalysis.WeblogAttackDetect();
        AnalysisDao analysisDao = new AnalysisDao();
//        for(int i=0;i<10;i++){
//            int id = (int)(Math.random()*530+1);
//            System.out.println(id);
//            analysisDao.updateWeblogURL(id,"中国成都");
//        }
    }
}
