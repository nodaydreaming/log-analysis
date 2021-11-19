package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class WeblogEntity implements Comparable<WeblogEntity>{
    private long id;
    private long srcip;
    private String producetime;
    private String url;
    private int reactnum;
    private String method;
    private String browser;
    private int times;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSrcip() {
        return srcip;
    }

    public void setSrcip(long srcip) {
        this.srcip = srcip;
    }

    public String getProducetime() {
        return producetime;
    }

    public void setProducetime(String producetime) {
        this.producetime = producetime;
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

    @Override
    public int compareTo(WeblogEntity o) {
        return this.producetime.compareTo(o.producetime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeblogEntity that = (WeblogEntity) o;
        return id == that.id &&
                srcip == that.srcip &&
                reactnum == that.reactnum &&
                times == that.times &&
                Objects.equals(producetime, that.producetime) &&
                Objects.equals(url, that.url) &&
                Objects.equals(method, that.method) &&
                Objects.equals(browser, that.browser) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, srcip, producetime, url, reactnum, method, browser, times, address);
    }
}
