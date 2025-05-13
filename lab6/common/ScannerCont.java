package lab6.common;

import java.util.Scanner;

/**
 * Класс для хранения статического экземпляра Scanner.
 * Используется для передачи экземпляра Scanner между классами.
 */
public class ScannerCont {
    private static Scanner scanner;

    /**
     * Устанавливает статический экземпляр Scanner.
     * @param scanner экземпляр Scanner
     */
    public static void setScanner(Scanner scanner) {
        ScannerCont.scanner = scanner;
    }
    
    /**
     * Возвращает статический экземпляр Scanner.
     * @return экземпляр Scanner
     */
    public static Scanner getScanner() {
        return scanner;
    }
}
