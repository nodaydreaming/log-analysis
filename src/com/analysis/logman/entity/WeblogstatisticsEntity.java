package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class WeblogstatisticsEntity {
    private int id;
    private long srcip;
    private Date starttime;
    private Date endtime;
    private String url;
    private int reactnum;
    private String method;
    private String browser;
    private int times;
    private String address;
    private Integer analysis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSrcip() {
        return srcip;
    }

    public void setSrcip(long srcip) {
        this.srcip = srcip;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReactnum() {
        return reactnum;
    }

    public void setReactnum(int reactnum) {
        this.reactnum = reactnum;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Integer analysis) {
        this.analysis = analysis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeblogstatisticsEntity that = (WeblogstatisticsEntity) o;
        return id == that.id &&
                srcip == that.srcip &&
                reactnum == that.reactnum &&
                times == that.times &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(url, that.url) &&
                Objects.equals(method, that.method) &&
                Objects.equals(browser, that.browser) &&
                Objects.equals(address, that.address) &&
                Objects.equals(analysis, that.analysis);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, srcip, starttime, endtime, url, reactnum, method, browser, times, address, analysis);
    }
}
