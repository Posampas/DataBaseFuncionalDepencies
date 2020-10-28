package pl.posmapas.key_dependency.model;

import java.util.Objects;

public class Attribute {
    String name;

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name);
    }


}
