package com.analysis.logman.entity;

import java.util.Objects;

public class EventstypeEntity {
    private int id;
    private String name;
    private String advice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventstypeEntity that = (EventstypeEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(advice, that.advice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, advice);
    }
}
