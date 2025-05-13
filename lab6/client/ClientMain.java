package lab6.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lab6.common.network.NetworkConfig;

/**
 * Главный класс для запуска клиента
 */
public class ClientMain {
    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getByName(NetworkConfig.DEFAULT_HOST);
            int port = NetworkConfig.DEFAULT_PORT;
            
            Client client = new Client(host, port);
            client.run();
            
        } catch (UnknownHostException e) {
            System.err.println("Ошибка: Не удалось определить адрес сервера: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: Неверный формат порта. Убедитесь, что в NetworkConfig указано число.");
        } catch (Exception e) {
             System.err.println("Произошла ошибка при запуске клиента: " + e.getMessage());
             e.printStackTrace();
        }
    }
} 