package lab6.client;

import lab6.common.models.Coordinates;
import lab6.common.models.Flat;
import lab6.common.models.House;
import lab6.common.models.enums.Furnish;
import lab6.common.models.enums.Transport;
import lab6.common.models.enums.View;

/**
 * Класс для чтения и валидации данных объектов
 */
public class InputReader {
    private final ScannerManager scannerManager;

    /**
     * Конструктор читателя ввода
     */
    public InputReader(ScannerManager scannerManager) {
        this.scannerManager = scannerManager;
    }

    /**
     * Читает данные для объекта Flat из консоли
     * @return созданный объект Flat
     */
    public Flat readFlat() {
        String name = readName();
        Coordinates coordinates = readCoordinates();
        Long area = readArea();
        int numberOfRooms = readNumberOfRooms();
        Furnish furnish = readFurnish();
        View view = readView();
        Transport transport = readTransport();
        House house = readHouse();
        
        return new Flat(name, coordinates, area, numberOfRooms, furnish, view, transport, house);
    }

    /**
     * Читает название квартиры
     * @return название
     */
    private String readName() {
        while (true) {
            String name = scannerManager.nextLine("Введите название квартиры: ");
            if (name.isEmpty()) {
                System.out.println("Название не может быть пустым");
                continue;
            }
            return name;
        }
    }

    /**
     * Читает координаты
     * @return объект Coordinates
     */
    private Coordinates readCoordinates() {
        System.out.println("Введите координаты:");
        
        double x;
        while (true) {
            x = scannerManager.nextDouble("  X (> -371): ");
            if (x <= -371) {
                System.out.println("Значение X должно быть больше -371");
                continue;
            }
            break;
        }
        
        int y = scannerManager.nextInt("  Y: ");
        
        return new Coordinates(x, y);
    }

    /**
     * Читает площадь квартиры
     * @return площадь
     */
    private Long readArea() {
        while (true) {
            long area = scannerManager.nextLong("Введите площадь (> 0): ");
            if (area <= 0) {
                System.out.println("Площадь должна быть больше 0");
                continue;
            }
            return area;
        }
    }

    /**
     * Читает количество комнат
     * @return количество комнат
     */
    private int readNumberOfRooms() {
        while (true) {
            int rooms = scannerManager.nextInt("Введите количество комнат (1-7): ");
            if (rooms <= 0 || rooms > 7) {
                System.out.println("Количество комнат должно быть в диапазоне от 1 до 7");
                continue;
            }
            return rooms;
        }
    }

    /**
     * Читает тип отделки
     * @return тип отделки
     */
    private Furnish readFurnish() {
        System.out.println("Доступные типы отделки:");
        Furnish[] values = Furnish.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i].name());
        }
        while (true) {
            String input = scannerManager.nextLine("Введите тип отделки (имя или номер, можно пропустить): ");
            if (input.isEmpty()) {
                return null;
            }
            try {
                int idx = Integer.parseInt(input);
                if (idx >= 1 && idx <= values.length) {
                    return values[idx - 1];
                } else {
                    System.out.println("Неверный номер типа отделки");
                }
            } catch (NumberFormatException e) {
                try {
                    return Furnish.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Неверный тип отделки");
                }
            }
        }
    }

    /**
     * Читает тип вида из окна
     * @return тип вида
     */
    private View readView() {
        System.out.println("Доступные типы вида из окна:");
        View[] values = View.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i].name());
        }
        while (true) {
            String input = scannerManager.nextLine("Введите тип вида (имя или номер, можно пропустить): ");
            if (input.isEmpty()) {
                return null;
            }
            try {
                int idx = Integer.parseInt(input);
                if (idx >= 1 && idx <= values.length) {
                    return values[idx - 1];
                } else {
                    System.out.println("Неверный номер типа вида");
                }
            } catch (NumberFormatException e) {
                try {
                    return View.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Неверный тип вида");
                }
            }
        }
    }

    /**
     * Читает тип транспортной доступности
     * @return тип транспортной доступности
     */
    private Transport readTransport() {
        System.out.println("Доступные типы транспортной доступности:");
        Transport[] values = Transport.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i].name());
        }
        while (true) {
            String input = scannerManager.nextLine("Введите тип транспортной доступности (имя или номер): ");
            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым");
                continue;
            }
            try {
                int idx = Integer.parseInt(input);
                if (idx >= 1 && idx <= values.length) {
                    return values[idx - 1];
                } else {
                    System.out.println("Неверный номер типа транспортной доступности");
                }
            } catch (NumberFormatException e) {
                try {
                    return Transport.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Неверный тип транспортной доступности");
                }
            }
        }
    }

    /**
     * Читает данные о доме
     * @return объект House
     */
    public House readHouse() {
        String input = scannerManager.nextLine("Хотите ввести информацию о доме? (yes/no): ");
        if (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("y")) {
            return null;
        }
        
        String name = scannerManager.nextLine("Введите название дома: ");
        if (name.isEmpty()) {
            System.out.println("Значение не может быть пустым");
            return null;
        }
        
        long year;
        while (true) {
            year = scannerManager.nextLong("Введите год постройки (> 0): ");
            if (year <= 0) {
                System.out.println("Год постройки должен быть больше 0");
                continue;
            }
            break;
        }
        
        int floors;
        while (true) {
            floors = scannerManager.nextInt("Введите количество этажей (> 0): ");
            if (floors <= 0) {
                System.out.println("Количество этажей должно быть больше 0");
                continue;
            }
            break;
        }
        
        return new House(name, year, floors);
    }
} 