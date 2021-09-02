package pl.redhat.samples.eventdriven.message;

public class CallmeEvent {

    private Integer id;
    private String message;
    private String eventType;

    public CallmeEvent() {
    }

    public CallmeEvent(Integer id, String message, String eventType) {
        this.id = id;
        this.message = message;
        this.eventType = eventType;
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

    @Override
    public String toString() {
        return "CallmeEvent{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
