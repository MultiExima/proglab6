package lab6.server;

import lab6.common.network.NetworkConfig;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.FileManager;
import lab6.server.commands.CommandManager;

/**
 * Главный класс для запуска сервера
 * @author Fedor
 * @version 1.0
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = NetworkConfig.DEFAULT_PORT;
        String filePath = "collection.xml";

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат порта. Используется порт по умолчанию: " + port);
            }
            
            if (args.length > 1) {
                filePath = args[1];
            }
        }
        
        CollectionManager collectionManager = new CollectionManager();
        FileManager fileManager = new FileManager(filePath);
        collectionManager.setCollection(fileManager.loadCollection());
        
        CommandManager commandManager = new CommandManager(collectionManager, fileManager);
        
        Server server = new Server(port, commandManager);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сохранение коллекции перед завершением работы...");
            fileManager.saveCollection(collectionManager.getCollection());
            server.stop();
            System.out.println("Сервер остановлен");
        }));
        
        server.start();
    }
} 