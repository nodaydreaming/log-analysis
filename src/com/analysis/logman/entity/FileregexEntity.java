package com.analysis.logman.entity;

import java.util.Objects;

public class FileregexEntity {
    private int id;
    private String type;
    private String regex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileregexEntity that = (FileregexEntity) o;
        return id == that.id &&
                Objects.equals(type, that.type) &&
                Objects.equals(regex, that.regex);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, regex);
    }
}
