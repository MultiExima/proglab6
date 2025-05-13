package lab6.server.collection;

import lab6.common.models.Flat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Класс для управления коллекцией на сервере
 */
public class CollectionManager {
    private Stack<Flat> collection;
    private final LocalDateTime creationDate;
    private Long nextId;

    /**
     * Конструктор менеджера коллекции
     */
    public CollectionManager() {
        this.collection = new Stack<>();
        this.creationDate = LocalDateTime.now();
        this.nextId = 1L;
    }

    /**
     * Устанавливает коллекцию
     * @param collection новая коллекция
     */
    public void setCollection(Stack<Flat> collection) {
        this.collection = collection;
        updateNextId();
    }

    /**
     * Возвращает коллекцию
     * @return коллекция
     */
    public Stack<Flat> getCollection() {
        return collection;
    }

    /**
     * Возвращает размер коллекции
     * @return размер коллекции
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * Возвращает дату создания коллекции
     * @return дата создания
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает тип коллекции
     * @return тип коллекции
     */
    public String getType() {
        return "Stack<Flat>";
    }

    /**
     * Добавляет элемент в коллекцию
     * @param flat элемент для добавления
     * @return добавленный элемент с установленным id
     */
    public Flat add(Flat flat) {
        flat.setId(generateNextId());
        if (flat.getCreationDate() == null) {
            flat.setCreationDate(LocalDateTime.now());
        }
        collection.push(flat);
        return flat;
    }

    /**
     * Обновляет элемент по id
     * @param id id элемента
     * @param newFlat новые данные
     * @return true если обновление успешно, иначе false
     */
    public boolean updateById(Long id, Flat newFlat) {
        for (int i = 0; i < collection.size(); i++) {
            Flat flat = collection.get(i);
            if (flat.getId().equals(id)) {
                newFlat.setId(id);
                newFlat.setCreationDate(flat.getCreationDate());
                collection.set(i, newFlat);
                return true;
            }
        }
        return false;
    }

    /**
     * Удаляет элемент по id
     * @param id id элемента
     * @return true если удаление успешно, иначе false
     */
    public boolean removeById(Long id) {
        return collection.removeIf(flat -> flat.getId().equals(id));
    }

    /**
     * Очищает коллекцию
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Удаляет первый элемент
     * @return true если удаление успешно, иначе false
     */
    public boolean removeFirst() {
        if (collection.isEmpty()) {
            return false;
        }
        collection.remove(0);
        return true;
    }

    /**
     * Перемешивает элементы коллекции
     */
    public void shuffle() {
        Collections.shuffle(collection);
    }

    /**
     * Удаляет элементы, меньшие чем заданный
     * @param flat элемент для сравнения
     * @return количество удалённых элементов
     */
    public int removeLower(Flat flat) {
        int sizeBefore = collection.size();
        collection = collection.stream()
                .filter(f -> f.compareTo(flat) >= 0)
                .collect(Collectors.toCollection(Stack::new));
        return sizeBefore - collection.size();
    }

    /**
     * Находит элемент по id
     * @param id id элемента
     * @return найденный элемент или null
     */
    public Flat getById(Long id) {
        return collection.stream()
                .filter(flat -> flat.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Обновляет nextId на основе текущей коллекции
     */
    private void updateNextId() {
        nextId = collection.stream()
                .map(Flat::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    /**
     * Генерирует следующий id
     * @return следующий id
     */
    private Long generateNextId() {
        return nextId++;
    }
} 