package pl.redhat.samples.eventdriven.gateway.message;

public class OrderCommand extends AbstractOrderCommand {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
