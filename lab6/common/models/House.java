package lab6.common.models;

import java.io.Serializable;

public class House implements Serializable {
    private String name; //Поле не может быть null
    private long year; //Значение поля должно быть больше 0
    private Integer numberOfFloors; //Значение поля должно быть больше 0

    public House() {}

    public House(String name, long year, Integer numberOfFloors) {
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    @Override
    public String toString() {
        return String.format(
            "%s (Год постройки: %d, Этажей: %d)",
            name, year, numberOfFloors
        );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (year ^ (year >>> 32));
        result = prime * result + ((numberOfFloors == null) ? 0 : numberOfFloors.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        House other = (House) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        
        if (year != other.year) return false;
        
        if (numberOfFloors == null) {
            if (other.numberOfFloors != null) return false;
        } else if (!numberOfFloors.equals(other.numberOfFloors)) return false;
        
        return true;
    }
} 
