package lab6.server.commands;

import java.io.Serializable;

import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для очистки коллекции
 */
public class ClearCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды очистки коллекции
     *
     * @param collectionManager менеджер коллекции
     */
    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду очистки коллекции
     *
     * @param args аргументы команды
     * @param data дополнительные данные (не используются)
     * @return ответ сервера
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        if (collectionManager.getSize() == 0) {
            return new Response("Коллекция уже пуста", true); // Успешный ответ, так как коллекция пуста
        }

        try {
            collectionManager.clear();
            return new Response("Коллекция успешно очищена", true);
        } catch (Exception e) {
            // Логирование ошибки
            return new Response("Ошибка при очистке коллекции: " + e.getMessage(), false);
        }
    }
} 
