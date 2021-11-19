package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class FwlogflowEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Integer curstream;
    private Integer upstream;
    private Integer downstream;
    private Integer abnormal;

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

    public Integer getCurstream() {
        return curstream;
    }

    public void setCurstream(Integer curstream) {
        this.curstream = curstream;
    }

    public Integer getUpstream() {
        return upstream;
    }

    public void setUpstream(Integer upstream) {
        this.upstream = upstream;
    }

    public Integer getDownstream() {
        return downstream;
    }

    public void setDownstream(Integer downstream) {
        this.downstream = downstream;
    }

    public Integer getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Integer abnormal) {
        this.abnormal = abnormal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FwlogflowEntity that = (FwlogflowEntity) o;
        return id == that.id &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(curstream, that.curstream) &&
                Objects.equals(upstream, that.upstream) &&
                Objects.equals(downstream, that.downstream) &&
                Objects.equals(abnormal, that.abnormal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, curstream, upstream, downstream, abnormal);
    }
}
