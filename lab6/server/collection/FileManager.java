package lab6.server.collection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Stack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lab6.common.models.Flat;
import lab6.common.models.enums.Furnish;
import lab6.common.models.enums.Transport;
import lab6.common.models.enums.View;

/**
 * Менеджер для работы с файлом коллекции на сервере.
 */
public class FileManager {
    private final String filePath;

    /**
     * Конструктор файлового менеджера
     * @param filePath Путь к файлу коллекции
     */
    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Создает и настраивает XmlMapper для сериализации и десериализации.
     * @return Настроенный XmlMapper
     */
    private XmlMapper createConfiguredMapper() {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        return mapper;
    }

    /**
     * Загружает коллекцию из файла.
     * @return Загруженная коллекция. Возвращает пустой стек при ошибке или если файл не существует/пуст.
     */
    public Stack<Flat> loadCollection() {
        Stack<Flat> collection = new Stack<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Файл коллекции '" + filePath + "' не найден. Будет создана пустая коллекция.");
            return collection;
        }
        if (!file.isFile()) {
            System.err.println("Путь '" + filePath + "' не является файлом.");
            return collection;
        }
        if (!file.canRead()) {
            System.err.println("Нет прав на чтение файла '" + filePath + "'.");
            return collection;
        }
        if (file.length() == 0) {
            System.out.println("Файл коллекции '" + filePath + "' пуст. Будет создана пустая коллекция.");
            return collection;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            StringBuilder xmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }

            String xml = xmlContent.toString().trim();

            if (xml.isEmpty() || xml.equals("<Stack/>") || xml.equals("<Stack></Stack>")) {
                System.out.println("Файл коллекции '" + filePath + "' содержит пустую коллекцию.");
                return collection;
            }

            XmlMapper xmlMapper = createConfiguredMapper();
            List<Flat> loadedList = xmlMapper.readValue(xml, new TypeReference<List<Flat>>() {});

            long maxId = 0;
            for (Flat flat : loadedList) {
                if (isValidFlat(flat)) {
                    collection.push(flat);
                    if (flat.getId() > maxId) {
                        maxId = flat.getId();
                    }
                } else {
                    System.err.println("Квартира с id " + flat.getId() + " из файла не прошла валидацию и не будет добавлена.");
                }
            }
            System.out.println("Коллекция успешно загружена из файла '" + filePath + "'. Загружено элементов: " + collection.size());

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла коллекции '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ошибка парсинга XML из файла '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        }

        return collection;
    }

    /**
     * Проверяет валидность данных квартиры.
     * @param flat Квартира для проверки
     * @return true, если квартира валидна, иначе false
     */
    private boolean isValidFlat(Flat flat) {
        if (flat == null) return false;
        try {
            if (flat.getId() == null || flat.getId() <= 0) return false;
            if (flat.getName() == null || flat.getName().trim().isEmpty()) return false;
            if (flat.getCoordinates() == null) return false;
            if (flat.getCoordinates().getX() == null || flat.getCoordinates().getX() <= -371) return false;
            if (flat.getCoordinates().getY() == null) return false;
            if (flat.getCreationDate() == null) return false;
            if (flat.getArea() == null || flat.getArea() <= 0) return false;
            if (flat.getNumberOfRooms() <= 0 || flat.getNumberOfRooms() > 7) return false;
            if (flat.getTransport() == null) return false;

            if (flat.getFurnish() != null) Furnish.valueOf(flat.getFurnish().toString());
            if (flat.getView() != null) View.valueOf(flat.getView().toString());
            Transport.valueOf(flat.getTransport().toString());

            if (flat.getHouse() != null) {
                if (flat.getHouse().getName() == null) return false;
                if (flat.getHouse().getYear() <= 0) return false;
                if (flat.getHouse().getNumberOfFloors() == null || flat.getHouse().getNumberOfFloors() <= 0) return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации квартиры (id=" + flat.getId() + "): Недопустимое значение enum: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при валидации квартиры (id=" + flat.getId() + "): " + e.getMessage());
            return false;
        }
    }

    /**
     * Сохраняет коллекцию в файл.
     * @param collection Коллекция для сохранения
     */
    public void saveCollection(Stack<Flat> collection) {
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                System.out.println("Создан новый файл для хранения коллекции: '" + filePath + "'");
            }
            if (!file.canWrite()) {
                System.err.println("Нет прав на запись в файл '" + filePath + "'. Коллекция не сохранена.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при проверке/создании файла '" + filePath + "' для записи: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {

            XmlMapper mapper = createConfiguredMapper();
            String xmlResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(collection);

            writer.write(xmlResult);
            writer.flush();
            System.out.println("Коллекция успешно сохранена в файл '" + filePath + "'. Сохранено " + collection.size() + " элементов.");

        } catch (IOException e) {
            System.err.println("Ошибка сохранения коллекции в файл '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при сериализации коллекции в XML для файла '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
