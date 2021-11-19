package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class FwlogeventsEntity {
    private long id;
    private Date starttime;
    private Date endtime;
    private Long srcip;
    private Long destip;
    private Integer destport;
    private int type;
    private String description;

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

    public Integer getDestport() {
        return destport;
    }

    public void setDestport(Integer destport) {
        this.destport = destport;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FwlogeventsEntity that = (FwlogeventsEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(srcip, that.srcip) &&
                Objects.equals(destip, that.destip) &&
                Objects.equals(destport, that.destport) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, starttime, endtime, srcip, destip, destport, type, description);
    }
}
