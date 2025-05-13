package lab6.common.models;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Double x; //Значение поля должно быть больше -371, Поле не может быть null
    private Integer y; //Поле не может быть null

    public Coordinates() {}

    public Coordinates(Double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return String.format("(x=%.2f, y=%d)", x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Coordinates other = (Coordinates) obj;
        if (x == null) {
            if (other.x != null) return false;
        } else if (!x.equals(other.x)) return false;
        
        if (y == null) {
            if (other.y != null) return false;
        } else if (!y.equals(other.y)) return false;
        
        return true;
    }
} 
