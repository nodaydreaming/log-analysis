package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class DestipanalysisEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Long destip;
    private Long totalnum;
    private Integer totalsrcipnum;
    private String srcips;
    private String counts;
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

    public Long getDestip() {
        return destip;
    }

    public void setDestip(Long destip) {
        this.destip = destip;
    }

    public Long getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(Long totalnum) {
        this.totalnum = totalnum;
    }

    public Integer getTotalsrcipnum() {
        return totalsrcipnum;
    }

    public void setTotalsrcipnum(Integer totalsrcipnum) {
        this.totalsrcipnum = totalsrcipnum;
    }

    public String getSrcips() {
        return srcips;
    }

    public void setSrcips(String srcips) {
        this.srcips = srcips;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
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
        DestipanalysisEntity that = (DestipanalysisEntity) o;
        return id == that.id &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(destip, that.destip) &&
                Objects.equals(totalnum, that.totalnum) &&
                Objects.equals(totalsrcipnum, that.totalsrcipnum) &&
                Objects.equals(srcips, that.srcips) &&
                Objects.equals(counts, that.counts) &&
                Objects.equals(isattack, that.isattack);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, destip, totalnum, totalsrcipnum, srcips, counts, isattack);
    }
}
