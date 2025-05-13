package lab6.server.commands;

import java.io.Serializable;

import lab6.common.models.Flat;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для удаления элементов, меньших чем заданный
 */
public class RemoveLowerCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды удаления элементов, меньших чем заданный
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления элементов, меньших чем заданный
     *
     * @param args аргументы команды
     * @param data объект Flat для сравнения
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция пуста", true);
        }

        if (!(data instanceof Flat)) {
            return new Response("Ошибка: Ожидался объект Flat в данных команды.", false);
        }

        Flat compareFlat = (Flat) data;
        if (compareFlat == null) {
            return new Response("Ошибка: Сравниваемый объект Flat не может быть null.", false);
        }

        try {
            int initialSize = collectionManager.getSize();
            // Предполагаем getCollection().removeIf и что Flat реализует Comparable<Flat>
            boolean removed = collectionManager.getCollection().removeIf(flat -> flat.compareTo(compareFlat) < 0);
            int removedCount = initialSize - collectionManager.getSize();

            if (removedCount > 0) {
                return new Response("Удалено элементов: " + removedCount, true);
            } else {
                return new Response("Нет элементов, меньших чем заданный", true);
            }
        } catch (Exception e) {
            // Логирование
            return new Response("Ошибка при удалении элементов: " + e.getMessage(), false);
        }
    }
} 
