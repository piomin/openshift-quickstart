package pl.redhat.samples.eventdriven.order.message;

import java.util.UUID;

public class OrderEvent {

    private String id;
    private String commandId;
    private String type;
    private String status;
    private String source;

    public OrderEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public OrderEvent(String commandId, String type, String status, String source) {
        this.id = UUID.randomUUID().toString();
        this.commandId = commandId;
        this.type = type;
        this.status = status;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
