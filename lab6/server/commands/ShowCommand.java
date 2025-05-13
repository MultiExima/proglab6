package lab6.server.commands;

import java.io.Serializable;
import java.util.Comparator;
import java.util.stream.Collectors;

import lab6.common.models.Flat;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для вывода всех элементов коллекции
 */
public class ShowCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды вывода всех элементов коллекции
     *
     * @param collectionManager менеджер коллекции
     */
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода всех элементов коллекции
     *
     * @param args аргументы команды (не используются)
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция пуста", true);
        }

        try {
            String collectionString = collectionManager.getCollection().stream()
                    .sorted(Comparator.comparing(Flat::getName))
                    .map(Object::toString)
                    .collect(Collectors.joining("\n--------------------\n"));

            return new Response("Элементы коллекции (отсортированы по названию):\n" + collectionString, true);
        } catch (Exception e) {
            // Логирование можно улучшить, добавив logger
            return new Response("Ошибка при получении элементов коллекции: " + e.getMessage(), false);
        }
    }
} 
