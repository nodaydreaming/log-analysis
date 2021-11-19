package com.analysis.logman.entity;

import java.util.Objects;

public class SensitiveportEntity {
    private int id;
    private Integer port;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
        SensitiveportEntity that = (SensitiveportEntity) o;
        return id == that.id &&
                Objects.equals(port, that.port) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, port, description);
    }
}
