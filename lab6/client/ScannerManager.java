package lab6.client;

import java.util.NoSuchElementException;
import java.util.Scanner;

import lab6.common.ScannerCont;

/**
 * Класс для управления вводом из консоли
 */
public class ScannerManager {
    private final Scanner scanner;

    /**
     * Конструктор менеджера сканера
     */
    public ScannerManager() {
        this.scanner = ScannerCont.getScanner();
    }

    /**
     * Считывает строку из консоли
     * @param prompt приглашение к вводу
     * @return введенная строка
     */
    public String nextLine(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("Ввод прерван");
            System.exit(1);
            return null;
        }
    }

    /**
     * Считывает целое число из консоли
     * @param prompt приглашение к вводу
     * @return введенное число
     */
    public int nextInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(nextLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число");
            }
        }
    }

    /**
     * Считывает длинное целое число из консоли
     * @param prompt приглашение к вводу
     * @return введенное число
     */
    public long nextLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(nextLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число");
            }
        }
    }

    /**
     * Считывает число с плавающей точкой из консоли
     * @param prompt приглашение к вводу
     * @return введенное число
     */
    public double nextDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(nextLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число с плавающей точкой");
            }
        }
    }

    /**
     * Закрывает сканер
     */
    public void close() {
        scanner.close();
    }
} 