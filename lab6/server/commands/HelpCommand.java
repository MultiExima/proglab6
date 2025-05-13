package lab6.server.commands;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import lab6.common.network.Response;

/**
 * Команда для получения справки по доступным командам
 */
public class HelpCommand extends ServerCommand {
    private final CommandManager commandManager;

    /**
     * Конструктор команды help
     * @param commandManager менеджер команд
     */
    public HelpCommand(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду help
     * @param args аргументы команды
     * @param data дополнительные данные
     * @return ответ с результатом выполнения
     */
    @Override
    public Response execute(String[] args, Serializable data) {
        Map<String, ServerCommand> commands = commandManager.getCommands();
        
        String helpText = commands.values().stream()
                .map(cmd -> String.format("%s - %s", cmd.getName(), cmd.getDescription()))
                .collect(Collectors.joining("\n"));
        
        return new Response(helpText, true);
    }
} 