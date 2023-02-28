package pl.redhat.samples.cpukiller.message;

public class PingEvent {
    private Long id;
    private String bigNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBigNumber() {
        return bigNumber;
    }

    public void setBigNumber(String bigNumber) {
        this.bigNumber = bigNumber;
    }

    @Override
    public String toString() {
        return "PingEvent{" +
                "id=" + id +
                ", bigNumber='" + bigNumber + '\'' +
                '}';
    }
}
