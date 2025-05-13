package lab6.server.commands;

import java.io.Serializable;

import lab6.common.models.Flat;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для обновления элемента коллекции по id
 */
public class UpdateCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды обновления элемента по id
     *
     * @param collectionManager менеджер коллекции
     */
    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции по его id");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду обновления элемента по id
     *
     * @param args аргументы команды
     * @param data дополнительные данные (новый объект Flat)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (args.length != 1) {
            return new Response("Использование: update id", false);
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            return new Response("id должен быть целым числом", false);
        }

        if (!(data instanceof Flat)) {
            return new Response("Ошибка: Ожидался объект Flat в данных команды.", false);
        }

        Flat newFlat = (Flat) data;
        if (newFlat == null) {
            return new Response("Ошибка: Новый объект Flat не может быть null.", false);
        }

        try {
            Flat oldFlat = collectionManager.getById(id);
            if (oldFlat == null) {
                return new Response("Элемент с id=" + id + " не найден", false);
            }

            newFlat.setId(id);
            newFlat.setCreationDate(oldFlat.getCreationDate());

            boolean removed = collectionManager.removeById(id);
            if (removed) {
                collectionManager.add(newFlat);
                return new Response("Элемент успешно обновлен", true);
            } else {
                return new Response("Не удалось обновить элемент (ошибка при удалении старого)", false);
            }
        } catch (Exception e) {
            return new Response("Ошибка при обновлении элемента: " + e.getMessage(), false);
        }
    }
} 
