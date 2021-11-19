package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class WebflowavgEntity {
    private int id;
    private Integer avgflow;
    private Date starttime;
    private Date endtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAvgflow() {
        return avgflow;
    }

    public void setAvgflow(Integer avgflow) {
        this.avgflow = avgflow;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebflowavgEntity that = (WebflowavgEntity) o;
        return id == that.id &&
                Objects.equals(avgflow, that.avgflow) &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, avgflow, starttime, endtime);
    }
}
