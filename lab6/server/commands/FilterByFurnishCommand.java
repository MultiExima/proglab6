package lab6.server.commands;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import lab6.common.models.Flat;
import lab6.common.models.enums.Furnish;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Команда для вывода элементов, значение поля furnish которых равно заданному
 */
public class FilterByFurnishCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды фильтрации по furnish
     *
     * @param collectionManager менеджер коллекции
     */
    public FilterByFurnishCommand(CollectionManager collectionManager) {
        super("filter_by_furnish", "вывести элементы, значение поля furnish которых равно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду фильтрации по furnish
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
            StringBuilder message = new StringBuilder("Использование: filter_by_furnish furnish\nДоступные значения furnish:\n");
            for (Furnish value : Furnish.values()) {
                message.append("  ").append(value.name()).append("\n");
            }
            return new Response(message.toString(), false);
        }

        Furnish furnish;
        try {
            furnish = Furnish.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            StringBuilder message = new StringBuilder("Некорректное значение furnish.\nДоступные значения:\n");
            for (Furnish value : Furnish.values()) {
                message.append("  ").append(value.name()).append("\n");
            }
            return new Response(message.toString(), false);
        }

        try {
            List<Flat> filteredList = collectionManager.getCollection().stream()
                    .filter(flat -> flat.getFurnish() == furnish)
                    .collect(Collectors.toList());

            if (filteredList.isEmpty()) {
                return new Response("Элементы с заданным значением furnish не найдены", true);
            } else {
                String resultString = filteredList.stream()
                                               .map(Object::toString)
                                               .collect(Collectors.joining("\n"));
                return new Response("Элементы с furnish = " + furnish + ":\n" + resultString, true);
            }
        } catch (Exception e) {
            return new Response("Ошибка при фильтрации: " + e.getMessage(), false);
        }
    }
} 
