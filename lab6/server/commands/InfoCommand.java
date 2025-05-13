package lab6.server.commands;

import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * Команда для получения информации о коллекции
 */
public class InfoCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды info
     * @param collectionManager менеджер коллекции
     */
    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду info
     * @param args аргументы команды
     * @param data дополнительные данные
     * @return ответ с результатом выполнения
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        
        String info = String.format(
                "Тип коллекции: %s\n" +
                "Дата инициализации: %s\n" +
                "Количество элементов: %d",
                collectionManager.getType(),
                collectionManager.getCreationDate().format(formatter),
                collectionManager.getSize()
        );
        
        return new Response(info, true);
    }
} 