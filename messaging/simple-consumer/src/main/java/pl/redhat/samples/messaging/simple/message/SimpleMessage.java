package pl.redhat.samples.messaging.simple.message;

import java.io.Serializable;

public class SimpleMessage implements Serializable {

    private Long id;
    private String source;
    private String content;

    public SimpleMessage() {
    }

    public SimpleMessage(Long id, String source, String content) {
        this.id = id;
        this.source = source;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
