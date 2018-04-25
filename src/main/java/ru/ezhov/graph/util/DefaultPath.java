package ru.ezhov.graph.util;

import java.util.Date;

public class DefaultPath implements Path {

    private String path;
    private Date date;

    @Override
    public String toString() {
        return path;
    }

    public DefaultPath(String path, Date date) {
        this.path = path;
        this.date = date;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Date date() {
        return date;
    }

    @Override
    public void date(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultPath that = (DefaultPath) o;

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
