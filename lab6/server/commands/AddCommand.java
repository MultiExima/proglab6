package lab6.server.commands;

import java.io.Serializable;

import lab6.common.models.Flat;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для добавления нового элемента в коллекцию
 * @author Fedor
 * @version 1.0
 */
public class AddCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса AddCommand
     * @param collectionManager - менеджер коллекции
     */
    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполнение команды добавления нового элемента в коллекцию
     * @param args - аргументы команды (не используются)
     * @param data - данные команды (ожидается объект Flat)
     * @return Ответ с результатом выполнения команды
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (!(data instanceof Flat)) {
            return new Response("Ошибка: Ожидался объект Flat в данных команды.", false);
        }
        
        Flat flat = (Flat) data;
        
        try {
            // Предполагаем, что CollectionManager имеет метод add
            collectionManager.add(flat); 
            return new Response("Квартира успешно добавлена в коллекцию", true);
        } catch (Exception e) {
            // Логирование ошибки на сервере может быть добавлено здесь
            return new Response("Ошибка при добавлении квартиры: " + e.getMessage(), false);
        }
    }
}
