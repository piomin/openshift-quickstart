package pl.redhat.samples.eventdriven.order.message;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderCommand extends AbstractOrderCommand {

    @Id
    private String id;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
