package com.analysis.logman.entity;

import java.util.Objects;

public class AssociationresultEntity {
    private int id;
    private String fwlogevents;
    private String weblogevents;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFwlogevents() {
        return fwlogevents;
    }

    public void setFwlogevents(String fwlogevents) {
        this.fwlogevents = fwlogevents;
    }

    public String getWeblogevents() {
        return weblogevents;
    }

    public void setWeblogevents(String weblogevents) {
        this.weblogevents = weblogevents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssociationresultEntity that = (AssociationresultEntity) o;
        return id == that.id &&
                Objects.equals(fwlogevents, that.fwlogevents) &&
                Objects.equals(weblogevents, that.weblogevents);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fwlogevents, weblogevents);
    }
}
