package lab6.common.network;

import java.io.Serializable;

/**
 * Класс, представляющий ответ от сервера клиенту.
 */
public class Response implements Serializable {
    private String message;
    private String collectionToStr;
    private boolean userAuthentication;

    public Response(String message, String collection) {
        this.message = message;
        this.collectionToStr = collection;
    }

    public Response(String message, boolean userAuthentication) {
        this.message = message;
        this.userAuthentication = userAuthentication;
    }

    public String getMessage() {
        return message;
    }   

    public String getCollectionToStr() {
        return collectionToStr;
    }

    public boolean isUserAuthentication() {
        return userAuthentication;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCollectionToStr(String collectionToStr) {
        this.collectionToStr = collectionToStr;
    }

    public void setUserAuthentication(boolean userAuthentication) {
        this.userAuthentication = userAuthentication;
    }
}