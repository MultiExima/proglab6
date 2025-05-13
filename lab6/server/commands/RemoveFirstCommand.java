package lab6.server.commands;

import java.io.Serializable;

import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для удаления первого элемента коллекции
 */
public class RemoveFirstCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды удаления первого элемента коллекции
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления первого элемента коллекции
     *
     * @param args аргументы команды (не используются)
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция пуста", false);
        }

        try {
            boolean removed = collectionManager.removeFirst();
            if (removed) {
                return new Response("Первый элемент успешно удален", true);
            } else {
                return new Response("Не удалось удалить первый элемент (возможно, коллекция пуста)", false);
            }
        } catch (Exception e) {
            return new Response("Ошибка при удалении первого элемента: " + e.getMessage(), false);
        }
    }
} 
