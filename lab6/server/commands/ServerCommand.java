package lab6.server.commands;

import java.io.Serializable;

import lab6.common.network.Response;

/**
 * Абстрактный класс серверной команды
 */
public abstract class ServerCommand {
    private final String name;
    private final String description;

    /**
     * Конструктор серверной команды
     * @param name название команды
     * @param description описание команды
     */
    public ServerCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Возвращает название команды
     * @return название команды
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Выполняет команду
     * @param args аргументы команды
     * @param data дополнительные данные
     * @return ответ с результатом выполнения
     */
    public abstract Response execute(String[] args, Serializable data);
} 