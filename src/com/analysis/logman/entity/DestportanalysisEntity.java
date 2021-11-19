package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class DestportanalysisEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Long srcip;
    private Long destip;
    private Integer totaldestiportnum;
    private String destports;
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

    public Long getDestip() {
        return destip;
    }

    public void setDestip(Long destip) {
        this.destip = destip;
    }

    public Integer getTotaldestiportnum() {
        return totaldestiportnum;
    }

    public void setTotaldestiportnum(Integer totaldestiportnum) {
        this.totaldestiportnum = totaldestiportnum;
    }

    public String getDestports() {
        return destports;
    }

    public void setDestports(String destports) {
        this.destports = destports;
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
        DestportanalysisEntity that = (DestportanalysisEntity) o;
        return id == that.id &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(srcip, that.srcip) &&
                Objects.equals(destip, that.destip) &&
                Objects.equals(totaldestiportnum, that.totaldestiportnum) &&
                Objects.equals(destports, that.destports) &&
                Objects.equals(counts, that.counts) &&
                Objects.equals(srcipaddress, that.srcipaddress) &&
                Objects.equals(isattack, that.isattack);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, srcip, destip, totaldestiportnum, destports, counts, srcipaddress, isattack);
    }
}
