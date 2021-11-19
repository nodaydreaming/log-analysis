package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class SrcipanalysisEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Long srcip;
    private Long totalnum;
    private Integer totaldestipnum;
    private String destips;
    private String counts;
    private String srcipaddress;
    private Integer isattack;

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

    public Long getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(Long totalnum) {
        this.totalnum = totalnum;
    }

    public Integer getTotaldestipnum() {
        return totaldestipnum;
    }

    public void setTotaldestipnum(Integer totaldestipnum) {
        this.totaldestipnum = totaldestipnum;
    }

    public String getDestips() {
        return destips;
    }

    public void setDestips(String destips) {
        this.destips = destips;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getSrcipaddress() {
        return srcipaddress;
    }

    public void setSrcipaddress(String srcipaddress) {
        this.srcipaddress = srcipaddress;
    }

    public Integer getIsattack() {
        return isattack;
    }

    public void setIsattack(Integer isattack) {
        this.isattack = isattack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SrcipanalysisEntity that = (SrcipanalysisEntity) o;
        return id == that.id &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(srcip, that.srcip) &&
                Objects.equals(totalnum, that.totalnum) &&
                Objects.equals(totaldestipnum, that.totaldestipnum) &&
                Objects.equals(destips, that.destips) &&
                Objects.equals(counts, that.counts) &&
                Objects.equals(srcipaddress, that.srcipaddress) &&
                Objects.equals(isattack, that.isattack);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, srcip, totalnum, totaldestipnum, destips, counts, srcipaddress, isattack);
    }
}
