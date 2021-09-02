package pl.redhat.samples.eventdriven.message;

public class CallmeEvent {

    private Integer id;
    private String messagex;
    private String eventType;

//    public CallmeEvent() {
//    }

    public CallmeEvent(Integer id, String messagex, String eventType) {
        this.id = id;
        this.messagex = messagex;
        this.eventType = eventType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessagex() {
        return messagex;
    }

    public void setMessagex(String messagex) {
        this.messagex = messagex;
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
                ", messagex='" + messagex + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
