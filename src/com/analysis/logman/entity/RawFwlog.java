package com.analysis.logman.entity;

import java.util.Date;

public class RawFwlog {
    private Long rawfwlogid;
    private String internalip;
    private Date producetime;
    private Date savetime;
    private String mathedstrategy;
    private String accessstrategy;
    private Long originalsrcip;
    private Integer originalsrcport;
    private Long originaldestip;
    private Integer originaldestport;
    private Long convertedsrcip;
    private Integer convertedsrcport;
    private Long converteddestip;
    private Integer converteddestport;
    private int protocolnumber;
    private Integer safetydomain;
    private Integer action;

    public Long getRawfwlogid() {
        return rawfwlogid;
    }

    public void setRawfwlogid(Long rawfwlogid) {
        this.rawfwlogid = rawfwlogid;
    }

    public String getInternalip() {
        return internalip;
    }

    public void setInternalip(String internalip) {
        this.internalip = internalip;
    }

    public Date getProducetime() {
        return producetime;
    }

    public void setProducetime(Date producetime) {
        this.producetime = producetime;
    }

    public Date getSavetime() {
        return savetime;
    }

    public void setSavetime(Date savetime) {
        this.savetime = savetime;
    }

    public String getMathedstrategy() {
        return mathedstrategy;
    }

    public void setMathedstrategy(String mathedstrategy) {
        this.mathedstrategy = mathedstrategy;
    }

    public String getAccessstrategy() {
        return accessstrategy;
    }

    public void setAccessstrategy(String accessstrategy) {
        this.accessstrategy = accessstrategy;
    }

    public Long getOriginalsrcip() {
        return originalsrcip;
    }

    public void setOriginalsrcip(Long originalsrcip) {
        this.originalsrcip = originalsrcip;
    }

    public Integer getOriginalsrcport() {
        return originalsrcport;
    }

    public void setOriginalsrcport(Integer originalsrcport) {
        this.originalsrcport = originalsrcport;
    }

    public Long getOriginaldestip() {
        return originaldestip;
    }

    public void setOriginaldestip(Long originaldestip) {
        this.originaldestip = originaldestip;
    }

    public Integer getOriginaldestport() {
        return originaldestport;
    }

    public void setOriginaldestport(Integer originaldestport) {
        this.originaldestport = originaldestport;
    }

    public Long getConvertedsrcip() {
        return convertedsrcip;
    }

    public void setConvertedsrcip(Long convertedsrcip) {
        this.convertedsrcip = convertedsrcip;
    }

    public Integer getConvertedsrcport() {
        return convertedsrcport;
    }

    public void setConvertedsrcport(Integer convertedsrcport) {
        this.convertedsrcport = convertedsrcport;
    }

    public Long getConverteddestip() {
        return converteddestip;
    }

    public void setConverteddestip(Long converteddestip) {
        this.converteddestip = converteddestip;
    }

    public Integer getConverteddestport() {
        return converteddestport;
    }

    public void setConverteddestport(Integer converteddestport) {
        this.converteddestport = converteddestport;
    }

    public int getProtocolnumber() {
        return protocolnumber;
    }

    public void setProtocolnumber(int protocolnumber) {
        this.protocolnumber = protocolnumber;
    }

    public Integer getSafetydomain() {
        return safetydomain;
    }

    public void setSafetydomain(Integer safetydomain) {
        this.safetydomain = safetydomain;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RawFwlog rawFwlog = (RawFwlog) o;

        if (protocolnumber != rawFwlog.protocolnumber) return false;
        if (rawfwlogid != null ? !rawfwlogid.equals(rawFwlog.rawfwlogid) : rawFwlog.rawfwlogid != null) return false;
        if (internalip != null ? !internalip.equals(rawFwlog.internalip) : rawFwlog.internalip != null) return false;
        if (producetime != null ? !producetime.equals(rawFwlog.producetime) : rawFwlog.producetime != null)
            return false;
        if (savetime != null ? !savetime.equals(rawFwlog.savetime) : rawFwlog.savetime != null) return false;
        if (mathedstrategy != null ? !mathedstrategy.equals(rawFwlog.mathedstrategy) : rawFwlog.mathedstrategy != null)
            return false;
        if (accessstrategy != null ? !accessstrategy.equals(rawFwlog.accessstrategy) : rawFwlog.accessstrategy != null)
            return false;
        if (originalsrcip != null ? !originalsrcip.equals(rawFwlog.originalsrcip) : rawFwlog.originalsrcip != null)
            return false;
        if (originalsrcport != null ? !originalsrcport.equals(rawFwlog.originalsrcport) : rawFwlog.originalsrcport != null)
            return false;
        if (originaldestip != null ? !originaldestip.equals(rawFwlog.originaldestip) : rawFwlog.originaldestip != null)
            return false;
        if (originaldestport != null ? !originaldestport.equals(rawFwlog.originaldestport) : rawFwlog.originaldestport != null)
            return false;
        if (convertedsrcip != null ? !convertedsrcip.equals(rawFwlog.convertedsrcip) : rawFwlog.convertedsrcip != null)
            return false;
        if (convertedsrcport != null ? !convertedsrcport.equals(rawFwlog.convertedsrcport) : rawFwlog.convertedsrcport != null)
            return false;
        if (converteddestip != null ? !converteddestip.equals(rawFwlog.converteddestip) : rawFwlog.converteddestip != null)
            return false;
        if (converteddestport != null ? !converteddestport.equals(rawFwlog.converteddestport) : rawFwlog.converteddestport != null)
            return false;
        if (safetydomain != null ? !safetydomain.equals(rawFwlog.safetydomain) : rawFwlog.safetydomain != null)
            return false;
        return action != null ? action.equals(rawFwlog.action) : rawFwlog.action == null;
    }

    @Override
    public int hashCode() {
        int result = rawfwlogid != null ? rawfwlogid.hashCode() : 0;
        result = 31 * result + (internalip != null ? internalip.hashCode() : 0);
        result = 31 * result + (producetime != null ? producetime.hashCode() : 0);
        result = 31 * result + (savetime != null ? savetime.hashCode() : 0);
        result = 31 * result + (mathedstrategy != null ? mathedstrategy.hashCode() : 0);
        result = 31 * result + (accessstrategy != null ? accessstrategy.hashCode() : 0);
        result = 31 * result + (originalsrcip != null ? originalsrcip.hashCode() : 0);
        result = 31 * result + (originalsrcport != null ? originalsrcport.hashCode() : 0);
        result = 31 * result + (originaldestip != null ? originaldestip.hashCode() : 0);
        result = 31 * result + (originaldestport != null ? originaldestport.hashCode() : 0);
        result = 31 * result + (convertedsrcip != null ? convertedsrcip.hashCode() : 0);
        result = 31 * result + (convertedsrcport != null ? convertedsrcport.hashCode() : 0);
        result = 31 * result + (converteddestip != null ? converteddestip.hashCode() : 0);
        result = 31 * result + (converteddestport != null ? converteddestport.hashCode() : 0);
        result = 31 * result + protocolnumber;
        result = 31 * result + (safetydomain != null ? safetydomain.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RawFwlog{" +
                "rawfwlogid=" + rawfwlogid +
                ", internalip='" + internalip + '\'' +
                ", producetime=" + producetime +
                ", savetime=" + savetime +
                ", mathedstrategy='" + mathedstrategy + '\'' +
                ", accessstrategy='" + accessstrategy + '\'' +
                ", originalsrcip=" + originalsrcip +
                ", originalsrcport=" + originalsrcport +
                ", originaldestip=" + originaldestip +
                ", originaldestport=" + originaldestport +
                ", convertedsrcip=" + convertedsrcip +
                ", convertedsrcport=" + convertedsrcport +
                ", converteddestip=" + converteddestip +
                ", converteddestport=" + converteddestport +
                ", protocolnumber=" + protocolnumber +
                ", safetydomain=" + safetydomain +
                ", action=" + action +
                '}';
    }
}
