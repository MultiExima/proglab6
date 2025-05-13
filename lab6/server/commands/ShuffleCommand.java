package lab6.server.commands;

import java.io.Serializable;

import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для перемешивания элементов коллекции в случайном порядке
 */
public class ShuffleCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды перемешивания элементов коллекции
     *
     * @param collectionManager менеджер коллекции
     */
    public ShuffleCommand(CollectionManager collectionManager) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду перемешивания элементов коллекции
     *
     * @param args аргументы команды (не используются)
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() <= 1) {
            return new Response("Коллекция пуста или содержит только один элемент, перемешивание не требуется", true);
        }

        try {
            // Предполагаем, что CollectionManager имеет метод shuffle
            collectionManager.shuffle();
            return new Response("Коллекция успешно перемешана", true);
        } catch (Exception e) {
            // Логирование
            return new Response("Ошибка при перемешивании коллекции: " + e.getMessage(), false);
        }
    }
} 
