package lab6.server.commands;

import java.util.HashMap;
import java.util.Map;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.FileManager;

/**
 * Класс для управления командами на сервере
 */
public class CommandManager {
    private final Map<String, ServerCommand> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    /**
     * Конструктор менеджера команд
     * @param collectionManager менеджер коллекции
     * @param fileManager менеджер файлов
     */
    public CommandManager(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
        registerCommands();
    }

    /**
     * Регистрирует все доступные команды
     */
    private void registerCommands() {
        commands.put("help", new HelpCommand(this));
        commands.put("info", new InfoCommand(collectionManager));
        commands.put("show", new ShowCommand(collectionManager));
        commands.put("add", new AddCommand(collectionManager));
        commands.put("update", new UpdateCommand(collectionManager));
        commands.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commands.put("clear", new ClearCommand(collectionManager));
        commands.put("remove_first", new RemoveFirstCommand(collectionManager));
        commands.put("remove_lower", new RemoveLowerCommand(collectionManager));
        commands.put("shuffle", new ShuffleCommand(collectionManager));
        commands.put("remove_all_by_view", new RemoveAllByViewCommand(collectionManager));
        commands.put("count_by_house", new CountByHouseCommand(collectionManager));
        commands.put("filter_by_furnish", new FilterByFurnishCommand(collectionManager));
    }

    /**
     * Выполняет команду по запросу
     * @param request запрос с информацией о команде
     * @return ответ с результатом выполнения команды
     */
    public Response executeCommand(Request request) {
        String commandName = request.getCommandName();
        ServerCommand command = commands.get(commandName);
        
        if (command == null) {
            System.err.println("Неизвестная команда: " + commandName);
            return new Response("Неизвестная команда: " + commandName, false);
        }
        
        try {
            System.out.println("Выполнение команды: " + commandName);
            return command.execute(request.getCommandStrArg(), request.getCommandObjArg());
        } catch (IllegalArgumentException e) {
            System.err.println("Неверные аргументы для команды " + commandName + ": " + e.getMessage());
            return new Response("Ошибка аргументов: " + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды " + commandName);
            return new Response("Внутренняя ошибка сервера при выполнении команды.", false);
        }
    }

    /**
     * Возвращает список всех команд с описаниями
     * @return карта команд
     */
    public Map<String, ServerCommand> getCommands() {
        return commands;
    }
} 