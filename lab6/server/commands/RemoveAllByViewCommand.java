package lab6.server.commands;

import java.io.Serializable;

import lab6.common.models.enums.View;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для удаления элементов с заданным значением поля view
 */
public class RemoveAllByViewCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды удаления элементов с заданным значением поля view
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveAllByViewCommand(CollectionManager collectionManager) {
        super("remove_all_by_view", "удалить из коллекции все элементы, значение поля view которого эквивалентно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления элементов с заданным значением поля view
     *
     * @param args аргументы команды
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция пуста", true);
        }

        if (args.length != 1) {
            return new Response("Использование: remove_all_by_view view", false);
        }

        View view;
        try {
            view = View.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Response("Некорректное значение view. Доступные значения: " +
                java.util.Arrays.toString(View.values()), false);
        }

        try {
            int initialSize = collectionManager.getSize();
            // Предполагаем, что CollectionManager имеет метод removeIf или используем getCollection().removeIf
            boolean removed = collectionManager.getCollection().removeIf(flat -> flat.getView() == view);
            int removedCount = initialSize - collectionManager.getSize();

            if (removedCount > 0) {
                return new Response("Удалено элементов: " + removedCount, true);
            } else {
                return new Response("Нет элементов с заданным значением view", true);
            }
        } catch (Exception e) {
            // Логирование
            return new Response("Ошибка при удалении элементов: " + e.getMessage(), false);
        }
    }
} 
