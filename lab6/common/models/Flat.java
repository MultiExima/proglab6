package lab6.common.models;

import lab6.common.models.enums.Furnish;
import lab6.common.models.enums.Transport;
import lab6.common.models.enums.View;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flat implements Comparable<Flat>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long area; //Значение поля должно быть больше 0
    private int numberOfRooms; //Максимальное значение поля: 7, Значение поля должно быть больше 0
    private Furnish furnish; //Поле может быть null
    private View view; //Поле может быть null
    private Transport transport; //Поле не может быть null
    private House house; //Поле может быть null

    public Flat() {}

    public Flat(String name, Coordinates coordinates, Long area, 
                int numberOfRooms, Furnish furnish, View view, 
                Transport transport, House house) {
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.creationDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    
    public Long getArea() { return area; }
    public void setArea(Long area) { this.area = area; }
    
    public int getNumberOfRooms() { return numberOfRooms; }
    public void setNumberOfRooms(int numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    
    public Furnish getFurnish() { return furnish; }
    public void setFurnish(Furnish furnish) { this.furnish = furnish; }
    
    public View getView() { return view; }
    public void setView(View view) { this.view = view; }
    
    public Transport getTransport() { return transport; }
    public void setTransport(Transport transport) { this.transport = transport; }
    
    public House getHouse() { return house; }
    public void setHouse(House house) { this.house = house; }

    // Сортировка по умолчанию
    @Override
    public int compareTo(Flat other) {
        if (other == null) {
            return -1;
        }
        
        // Сравнение по площади
        int areaCompare = this.area.compareTo(other.area);
        if (areaCompare != 0) {
            return areaCompare;
        }
        
        // Сравнение по количеству комнат
        int roomsCompare = Integer.compare(this.numberOfRooms, other.numberOfRooms);
        if (roomsCompare != 0) {
            return roomsCompare;
        }
        
        // Сравнение по названию
        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }
        
        // Сравнение по id
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format(
            "id Квартиры: %d\n" +
            "Название: %s\n" +
            "Координаты: %s\n" +
            "Дата создания: %s\n" +
            "Площадь: %d кв.м\n" +
            "Количество комнат: %d\n" +
            "Отделка: %s\n" +
            "Вид: %s\n" +
            "Транспорт: %s\n" +
            "Дом: %s",
            id, name, coordinates.toString(), creationDate, 
            area, numberOfRooms, furnish, view,
            transport, house != null ? house.toString() : "не указано"
        );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + numberOfRooms;
        result = prime * result + ((furnish == null) ? 0 : furnish.hashCode());
        result = prime * result + ((view == null) ? 0 : view.hashCode());
        result = prime * result + ((transport == null) ? 0 : transport.hashCode());
        result = prime * result + ((house == null) ? 0 : house.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Flat other = (Flat) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        
        if (coordinates == null) {
            if (other.coordinates != null) return false;
        } else if (!coordinates.equals(other.coordinates)) return false;
        
        if (creationDate == null) {
            if (other.creationDate != null) return false;
        } else if (!creationDate.equals(other.creationDate)) return false;
        
        if (area == null) {
            if (other.area != null) return false;
        } else if (!area.equals(other.area)) return false;
        
        if (numberOfRooms != other.numberOfRooms) return false;
        
        if (furnish == null) {
            if (other.furnish != null) return false;
        } else if (!furnish.equals(other.furnish)) return false;
        
        if (view == null) {
            if (other.view != null) return false;
        } else if (!view.equals(other.view)) return false;
        
        if (transport == null) {
            if (other.transport != null) return false;
        } else if (!transport.equals(other.transport)) return false;
        
        if (house == null) {
            if (other.house != null) return false;
        } else if (!house.equals(other.house)) return false;
        
        return true;
    }
} 