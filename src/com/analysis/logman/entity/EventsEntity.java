package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class EventsEntity {
    private int id;
    private Date starttime;
    private Date endtime;
    private int type;
    private String description;
    private Integer analysis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        EventsEntity that = (EventsEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(description, that.description) &&
                Objects.equals(analysis, that.analysis);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, type, description, analysis);
    }
}
