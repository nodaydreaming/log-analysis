package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class AssociationeventsEntity {
    private int id;
    private Long srcip;
    private Date starttime;
    private Date endtime;
    private String fwlogevents;
    private String webevents;
    private String srcipaddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getSrcip() {
        return srcip;
    }

    public void setSrcip(Long srcip) {
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

    public String getFwlogevents() {
        return fwlogevents;
    }

    public void setFwlogevents(String fwlogevents) {
        this.fwlogevents = fwlogevents;
    }

    public String getWebevents() {
        return webevents;
    }

    public void setWebevents(String webevents) {
        this.webevents = webevents;
    }

    public String getSrcipaddress() {
        return srcipaddress;
    }

    public void setSrcipaddress(String srcipaddress) {
        this.srcipaddress = srcipaddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssociationeventsEntity that = (AssociationeventsEntity) o;
        return id == that.id &&
                Objects.equals(srcip, that.srcip) &&
                Objects.equals(starttime, that.starttime) &&
                Objects.equals(endtime, that.endtime) &&
                Objects.equals(fwlogevents, that.fwlogevents) &&
                Objects.equals(webevents, that.webevents) &&
                Objects.equals(srcipaddress, that.srcipaddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, srcip, starttime, endtime, fwlogevents, webevents, srcipaddress);
    }
}
