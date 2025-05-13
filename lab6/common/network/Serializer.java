package lab6.common.network;

import java.io.*;

/**
 * Утилитарный класс для сериализации/десериализации объектов
 */
public class Serializer {
    
    /**
     * Сериализует объект в массив байтов
     *
     * @param object объект для сериализации
     * @return массив байтов
     * @throws IOException если возникла ошибка ввода/вывода
     */
    public static byte[] serialize(Serializable object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        }
    }
    
    /**
     * Десериализует объект из массива байтов
     *
     * @param bytes массив байтов
     * @return десериализованный объект
     * @throws IOException если возникла ошибка ввода/вывода
     * @throws ClassNotFoundException если класс не найден
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            
            return objectInputStream.readObject();
        }
    }
} 