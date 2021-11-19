package com.analysis.logman.entity;

import java.util.Date;
import java.util.Objects;

public class FwlogEntity {
    private long id;
    private long internalip;
    private Date producetime;
    private String mathedstrategy;
    private String accessstrategy;
    private long srcip;
    private int srcport;
    private long destip;
    private int destport;
    private int protocol;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInternalip() {
        return internalip;
    }

    public void setInternalip(long internalip) {
        this.internalip = internalip;
    }

    public Date getProducetime() {
        return producetime;
    }

    public void setProducetime(Date producetime) {
        this.producetime = producetime;
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

    public long getSrcip() {
        return srcip;
    }

    public void setSrcip(long srcip) {
        this.srcip = srcip;
    }

    public int getSrcport() {
        return srcport;
    }

    public void setSrcport(int srcport) {
        this.srcport = srcport;
    }

    public long getDestip() {
        return destip;
    }

    public void setDestip(long destip) {
        this.destip = destip;
    }

    public int getDestport() {
        return destport;
    }

    public void setDestport(int destport) {
        this.destport = destport;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FwlogEntity that = (FwlogEntity) o;
        return id == that.id &&
                internalip == that.internalip &&
                srcip == that.srcip &&
                srcport == that.srcport &&
                destip == that.destip &&
                destport == that.destport &&
                protocol == that.protocol &&
                Objects.equals(producetime, that.producetime) &&
                Objects.equals(mathedstrategy, that.mathedstrategy) &&
                Objects.equals(accessstrategy, that.accessstrategy) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, internalip, producetime, mathedstrategy, accessstrategy, srcip, srcport, destip, destport, protocol, address);
    }
}
