package lab6.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.common.network.Serializer;
import lab6.server.commands.CommandManager;

/**
 * Сервер для обработки клиентских запросов
 */
public class Server {
    private final int port;
    private final CommandManager commandManager;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private boolean running;

    private final Map<SocketChannel, ClientState> clientStates = new HashMap<>();

    private static class ClientState {
        ByteBuffer readBuffer = ByteBuffer.allocate(8192);
        ByteBuffer writeBuffer = null;
        Integer expectedLength = null;
    }

    /**
     * Конструктор сервера
     * @param port порт для прослушивания
     * @param commandManager менеджер команд
     */
    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.running = false;
    }

    /**
     * Запускает сервер
     */
    public void start() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            running = true;
            System.out.println("Сервер запущен на порту " + port);

            while (running && selector.isOpen()) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка селектора: " + e.getMessage());
                    break;
                }


                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    try {
                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            read(key);
                        } else if (key.isWritable()) {
                            write(key);
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка обработки клиента: " + e.getMessage());
                        closeClientConnection(key);
                    } catch (Exception e) {
                        System.err.println("Критическая ошибка обработки клиента: " + e.getMessage());
                        closeClientConnection(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Принимает новое соединение от клиента
     * @param key ключ селектора
     * @throws IOException ошибка ввода-вывода
     */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = ssc.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            clientStates.put(clientChannel, new ClientState());
            System.out.println("Новое соединение: " + clientChannel.getRemoteAddress());
        }
    }

    /**
     * Читает данные от клиента
     * @param key ключ селектора
     * @throws IOException ошибка ввода-вывода
     * @throws ClassNotFoundException ошибка десериализации
     */
    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientState state = clientStates.get(clientChannel);
        if (state == null) return;

        int bytesRead = clientChannel.read(state.readBuffer);
        if (bytesRead == -1) {
            closeClientConnection(key);
            return;
        }
        if (bytesRead == 0) {
            return;
        }

        state.readBuffer.flip();

        
        while (true) {
            if (state.expectedLength == null) {
                if (state.readBuffer.remaining() >= 4) {
                    state.expectedLength = state.readBuffer.getInt();
                } else {
                    break;
                }
            }

            if (state.expectedLength != null && state.readBuffer.remaining() >= state.expectedLength) {
                
                byte[] data = new byte[state.expectedLength];
                state.readBuffer.get(data);

                Request request = (Request) Serializer.deserialize(data);
                System.out.println("Получен запрос: " + request + " от " + clientChannel.getRemoteAddress());

                Response response = (Response) commandManager.executeCommand(request);

                byte[] responseData = Serializer.serialize(response);
                state.writeBuffer = ByteBuffer.allocate(4 + responseData.length);
                state.writeBuffer.putInt(responseData.length);
                state.writeBuffer.put(responseData);
                state.writeBuffer.flip();

                state.expectedLength = null;

                key.interestOps(SelectionKey.OP_WRITE);

            } else {
                break;
            }
        }

        state.readBuffer.compact();
    }

    /**
     * Отправляет данные клиенту
     * @param key ключ селектора
     * @throws IOException ошибка ввода-вывода
     */
    private void write(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientState state = clientStates.get(clientChannel);
        if (state == null || state.writeBuffer == null) return;

        int bytesWritten = clientChannel.write(state.writeBuffer);


        if (!state.writeBuffer.hasRemaining()) {
            System.out.println("Отправлен ответ клиенту " + clientChannel.getRemoteAddress());
            state.writeBuffer = null;
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     * Закрывает соединение с клиентом
     * @param key ключ селектора
     */
    private void closeClientConnection(SelectionKey key) {
         SocketChannel clientChannel = (SocketChannel) key.channel();
         if (clientChannel != null) {
             clientStates.remove(clientChannel);
             try {
                 System.out.println("Закрытие соединения с клиентом: " + clientChannel.getRemoteAddress());
                 key.cancel();
                 clientChannel.close();
             } catch (IOException e) {
                 System.err.println("Ошибка при закрытии соединения с клиентом: " + e.getMessage());
             }
         }
    }

    /**
     * Останавливает сервер
     */
    public void stop() {
        running = false;
        try {
            if (selector != null && selector.isOpen()) {
                for (SelectionKey key : selector.keys()) {
                    if (key.channel() instanceof SocketChannel && key.isValid()) {
                         closeClientConnection(key);
                     }
                }
                selector.close();
                System.out.println("Селектор остановлен");
            }
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
                System.out.println("Серверный канал остановлен");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при остановке сервера: " + e.getMessage());
        }
    }
} 