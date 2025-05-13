package lab6.server.commands;

import java.io.Serializable;

import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для удаления элемента коллекции по id
 */
public class RemoveByIdCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды удаления элемента по id
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления элемента по id
     *
     * @param args аргументы команды
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (args.length != 1) {
            return new Response("Использование: remove_by_id id", false);
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            return new Response("id должен быть целым числом", false);
        }

        try {
            // Проверяем наличие элемента перед удалением (опционально, removeById может это делать)
            if (collectionManager.getById(id) == null) {
                 return new Response("Элемент с id=" + id + " не найден", false);
            }
            
            // Предполагаем, что removeById возвращает boolean
            if (collectionManager.removeById(id)) {
                return new Response("Элемент с id=" + id + " успешно удален", true);
            } else {
                // Эта ветка может быть недостижима, если getById проверка надежна
                return new Response("Не удалось удалить элемент с id=" + id + " (возможно, он был удален другим запросом)", false);
            }
        } catch (Exception e) {
            // Логирование
            return new Response("Ошибка при удалении элемента: " + e.getMessage(), false);
        }
    }
} 
