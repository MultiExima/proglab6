package lab6.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import lab6.common.ScannerCont;
import lab6.common.exceptions.RecursionException;
import lab6.common.models.Flat;
import lab6.common.models.House;
import lab6.common.network.Request;
import lab6.common.network.Response;

/*
 * Класс, реализующий клиентское приложение
 * Содержит методы для обработки пользовательского ввода
 * и отправки запросов на сервер
 * @author Fedor
 * @version 1.0
 */

public class Client {
    private final Set<Path> scriptsNames = new TreeSet<>();
    private final InetAddress host;
    private final int port;
    private SocketChannel channel;
    private ScannerManager scannerManager;

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            SocketAddress address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);

            ScannerCont.setScanner(new Scanner(System.in));
            var scanner = ScannerCont.getScanner();
            this.scannerManager = new ScannerManager();

            System.out.println("Клиентское приложение запущено. Для просмотра доступных команд введите 'help'");

            while (true) {
                System.out.print(">>> ");
                try {
                    while (scanner.hasNext()) {
                        var command = "";
                        var arguments = "";
                        String[] input = (scanner.nextLine() + " ").trim().split(" ");

                        command = input[0].trim();
                        processUserPrompt(command, Arrays.copyOfRange(input, 1, input.length));
                        System.out.print(">>> ");
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("Остановка клиента через консоль");
                    exit(1);
                } catch (ClassNotFoundException e) {
                    System.out.println("Объект поступивший в ответ от сервера не найден");
                } catch (SocketException e) {
                    System.out.println("Сервер был остановлен во время обработки вашего запроса. Пожалуйста, повторите попытку позже");
                    exit(1);
                }
            }

        } catch (ConnectException e) {
            System.out.println("Сервер недоступен в данный момент. Пожалуйста, повторите попытку позже");
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
    }

    private void processUserPrompt(String command, String[] arguments) throws IOException, ClassNotFoundException {
        Request request;
        if (command.equalsIgnoreCase("add") || command.equalsIgnoreCase("update") || command.equalsIgnoreCase("remove_lower")) {
            Flat objArgument = new InputReader(this.scannerManager).readFlat();
            request = new Request(command, arguments, objArgument);
            Response response = sendAndReceive(request);
            printResponse(response);
        } else if (command.equalsIgnoreCase("count_by_house")) {
            House objArgument = new InputReader(this.scannerManager).readHouse();
            request = new Request(command, arguments, objArgument);
            Response response = sendAndReceive(request);
            printResponse(response);
        } else if (command.equalsIgnoreCase("exit")) {
            System.out.println("Работа клиентского приложения завершена");
            exit(0);
        } else if (command.equalsIgnoreCase("execute_script")) {
            if (arguments.length == 0) {
                System.out.println("Неверные аргументы команды");
            } else if (arguments.length == 1) {
                executeScript(arguments[0]);
            } else {
                System.out.println("Неверное количество аргументов");
            }
        } else {
            request = new Request(command, arguments);
            Response response = sendAndReceive(request);
            printResponse(response);
        }
    }

    private Response sendAndReceive(Request request) throws IOException, ClassNotFoundException {

        byte[] requestBytes;
        try(ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(request);
            requestBytes = bytes.toByteArray();
        }

        int requestLength = requestBytes.length;

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4).putInt(requestLength).flip();
        while (lengthBuffer.hasRemaining()) {
             channel.write(lengthBuffer);
        }
       
        ByteBuffer dataToSend = ByteBuffer.wrap(requestBytes);
        while (dataToSend.hasRemaining()) {
             channel.write(dataToSend);
        }
        
        ByteBuffer dataToReceiveLength = ByteBuffer.allocate(4);
        int bytesReadLength = channel.read(dataToReceiveLength);

        if (bytesReadLength == -1) { 
            throw new IOException("Соединение закрыто сервером перед получением длины ответа.");
        }
        if (bytesReadLength < 4) {
             while(dataToReceiveLength.hasRemaining()) {
                 int readMore = channel.read(dataToReceiveLength);
                 if (readMore == -1) {
                      throw new IOException("Соединение закрыто сервером во время чтения длины ответа.");
                 }
                 bytesReadLength += readMore;
             }
             if (bytesReadLength < 4) {
                 throw new IOException("Не удалось прочитать полную длину ответа от сервера (прочитано " + bytesReadLength + " байт).");
             }
        }

        dataToReceiveLength.flip();
        int responseLength = dataToReceiveLength.getInt();

        if (responseLength < 0) {
            throw new IOException("Получена некорректная (отрицательная) длина ответа от сервера: " + responseLength);
        }
         if (responseLength == 0) {
             System.out.println("Сервер прислал ответ нулевой длины.");
             return null; 
         }


        ByteBuffer responseBytes = ByteBuffer.allocate(responseLength); // Allocate buffer for the response object
        int totalBytesRead = 0;
        while (totalBytesRead < responseLength) {
            int bytesRead = channel.read(responseBytes);
            if (bytesRead == -1) {
                throw new IOException("Соединение закрыто сервером во время чтения данных ответа (ожидалось " + responseLength + ", прочитано " + totalBytesRead + ").");
            }
            totalBytesRead += bytesRead;
        }
        responseBytes.flip();

        try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(responseBytes.array(), 0, totalBytesRead))){
           return (Response) in.readObject();
        }
    }

    private void printResponse(Response response) {
        System.out.println(response.getMessage());
        String collection = response.getCollectionToStr();
        try{
            if (!collection.isEmpty()){
                System.out.println(collection);
            }
        } catch (NullPointerException ignored){}
    }

    private void executeScript(String path) throws ClassNotFoundException {
        if (path.isBlank()) {
            System.out.println("Вы не указали путь к скрипту");

        } else {
            try {
                Path pathToScript = Paths.get(path);

                ScannerCont.setScanner(new Scanner(pathToScript));
                Scanner scriptScanner = ScannerCont.getScanner();

                Path scriptFile = pathToScript.getFileName();

                if (!scriptScanner.hasNext()) throw new NoSuchElementException();

                scriptsNames.add(scriptFile);

                do {
                    var command = "";
                    String[] input = (scriptScanner.nextLine() + " ").trim().split(" ");
                    command = input[0].trim();

                    while (scriptScanner.hasNextLine() && command.isEmpty()) {
                        input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                        command = input[0].trim();
                    }

                    if (command.equalsIgnoreCase("execute_script")) {
                        try {
                            Path scriptNameFromArgument = Paths.get(input[1]).getFileName();

                            if (scriptsNames.contains(scriptNameFromArgument)) {
                                throw new RecursionException("Обнаружена попытка рекурсивного выполнения скрипта");
                            }
                            executeScript(input[1]);

                        } catch (RecursionException e) {
                            System.out.println(e.getMessage());
                        }

                    } else {
                        processUserPrompt(command, Arrays.copyOfRange(input, 1, input.length));
                    }

                } while (scriptScanner.hasNextLine());

                scriptsNames.remove(scriptFile);
                ScannerCont.setScanner(new Scanner(System.in));
                System.out.println("Скрипт " + scriptFile + " успешно выполнен");

            } catch (FileNotFoundException e) {
                System.out.println("Файл " + path + " не найден");
            } catch (NoSuchElementException e) {
                System.out.println("Файл " + path + " не содержит команд");
            } catch (IllegalStateException e) {
                System.out.println("Непредвиденная ошибка");
            } catch (SecurityException e) {
                System.out.println("Нет прав для чтения файла " + path);
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
                System.out.println(e.getClass().getName());
            } catch (InvalidPathException e) {
                System.out.println("Проверьте путь к файлу. В нём не должно быть лишних символов");
            }
        }
    }
}