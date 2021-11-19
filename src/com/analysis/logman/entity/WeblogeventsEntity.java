package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class WeblogeventsEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Long srcip;
    private long weblogid;
    private Integer type;
    private String attackaddress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getSrcip() {
        return srcip;
    }

    public void setSrcip(Long srcip) {
        this.srcip = srcip;
    }

    public long getWeblogid() {
        return weblogid;
    }

    public void setWeblogid(long weblogid) {
        this.weblogid = weblogid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAttackaddress() {
        return attackaddress;
    }

    public void setAttackaddress(String attackaddress) {
        this.attackaddress = attackaddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeblogeventsEntity that = (WeblogeventsEntity) o;
        return id == that.id &&
                weblogid == that.weblogid &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(srcip, that.srcip) &&
                Objects.equals(type, that.type) &&
                Objects.equals(attackaddress, that.attackaddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, srcip, weblogid, type, attackaddress);
    }
}
