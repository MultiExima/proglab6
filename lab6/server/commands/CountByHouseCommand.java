package lab6.server.commands;

import java.io.Serializable;

import lab6.common.models.House;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для подсчета элементов с заданным значением поля house
 */
public class CountByHouseCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды подсчета элементов с заданным значением поля house
     *
     * @param collectionManager менеджер коллекции
     */
    public CountByHouseCommand(CollectionManager collectionManager) {
        super("count_by_house", "вывести количество элементов, значение поля house которых равно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду подсчета элементов с заданным значением поля house
     *
     * @param args аргументы команды
     * @param data объект House для сравнения
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция пуста", true);
        }

        House houseToCompare;
        if (data == null) {
            houseToCompare = new House("", 0, 0L);
        } else if (!(data instanceof House)) {
            return new Response("Ошибка: Ожидался объект House в данных команды.", false);
        } else {
            houseToCompare = (House) data;
        }

        try {
            long count = collectionManager.getCollection().stream()
                    .filter(flat -> flat.getHouse() != null && flat.getHouse().equals(houseToCompare))
                    .count();
            return new Response("Количество элементов с заданным домом: " + count, true);
        } catch (Exception e) {
            return new Response("Ошибка при подсчете элементов: " + e.getMessage(), false);
        }
    }
} 
