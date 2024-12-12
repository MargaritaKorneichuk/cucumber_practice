package org.example.pojos;

import java.util.Objects;

public class ProductPOJO {
    private String name;
    private String type;
    private boolean exotic;

    public ProductPOJO(String name, String type, boolean exotic){
        this.name = name;
        this.type = type;
        this.exotic = exotic;
    }
    public ProductPOJO(){}
    public boolean isExotic() {
        return exotic;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setExotic(boolean exotic) {
        this.exotic = exotic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPOJO that = (ProductPOJO) o;
        return this.exotic == that.exotic &&
                this.name.equals(that.name) &&
                this.type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, exotic);
    }
}
