package pl.redhat.samples.eventdriven.message;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LargeEvent {
    private Integer id;
    private String message;
    private String eventType;
    private int size;

    public LargeEvent() {
        byte[] array = new byte[2000];
        new Random().nextBytes(array);
        message = new String(array, StandardCharsets.UTF_8);
    }

    public LargeEvent(Integer id, String eventType, int size) {
        this.id = id;
        this.eventType = eventType;
        this.size = size;
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        message = new String(array, StandardCharsets.UTF_8);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
