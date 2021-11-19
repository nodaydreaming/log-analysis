package com.analysis.logman.entity;

import java.util.Objects;

public class LogfileEntity {
    private int id;
    private String address;
    private String filename;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogfileEntity that = (LogfileEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(address, that.address) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, address, filename, type);
    }
}
