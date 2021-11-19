package com.analysis.logman.entity;

/**
 * 用来存放src和dest ip 端口
 */
public class SrcDest {
    private Long srcip;
    private Long destip;
    private Integer destport;

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

    @Override
    public String toString() {
        return srcip + "," + destip + "," + destport;
    }
}
