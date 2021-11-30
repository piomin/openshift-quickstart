package functions;

import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;

public class Function {

    @Inject
    Logger logger;
    @Inject
    @RestClient
    BrokerClient client;

    @Funq
    public CloudEvent<Output> function(CloudEvent<Input> input) {
        logger.infof("New event: %s", input);
        Output output = new Output(input.data().getMessage());
        CloudEvent<Output> outputCloudEvent = CloudEventBuilder.create().build(output);
        client.sendEvent(output,
                input.id(),
                "http://caller-function",
                "caller.output",
                input.specVersion());

        return outputCloudEvent;
    }

}
