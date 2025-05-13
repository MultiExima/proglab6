package lab6.common.network;

import java.io.Serializable;

/**
 * Класс, представляющий запрос от клиента к серверу.
 */
public class Request implements Serializable {
    private String commandName;
    private String[] commandStrArg;
    private Serializable commandObjArg;

    public Request(String commandName, String[] commandStrArg, Serializable commandObjArg) {
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
        this.commandObjArg = commandObjArg;
    }

    public Request(String commandName, String[] commandStrArg) {
        this(commandName, commandStrArg, null);
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getCommandStrArg() {
        return commandStrArg;
    }

    public Serializable getCommandObjArg() {
        return commandObjArg;
    }
}