package functions;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/piomin-serverless/default")
@RegisterRestClient
public interface BrokerClient {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String sendEvent(Output event,
                     @HeaderParam("Ce-Id") String id,
                     @HeaderParam("Ce-Source") String source,
                     @HeaderParam("Ce-Type") String type,
                     @HeaderParam("Ce-Specversion") String version);
}
