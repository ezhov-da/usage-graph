package ru.ezhov.graph.storage;

import java.util.Date;

public class DefaultPath implements Path {

    private String path;
    private Date date;

    @Override
    public String toString() {
        return path;
    }

    public DefaultPath(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultPath that = (DefaulktPath) o;

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
