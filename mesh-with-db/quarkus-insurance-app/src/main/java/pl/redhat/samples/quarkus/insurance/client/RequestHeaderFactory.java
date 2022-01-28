package pl.redhat.samples.quarkus.insurance.client;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class RequestHeaderFactory implements ClientHeadersFactory {

    @Inject
    Logger log;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        String version = incomingHeaders.getFirst("x-version");
        log.infof("Version Header: %s", version);
        String traceId = incomingHeaders.getFirst("x-b3-traceid");
        log.infof("Trace Header: %s", traceId);
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("X-Version", version);
        result.add("X-B3-TraceId", traceId);
        result.add("X-Request-Id", incomingHeaders.getFirst("x-request-id"));
        result.add("X-B3-SpanId", incomingHeaders.getFirst("x-b3-spanid"));
        result.add("X-B3-ParentSpanId", incomingHeaders.getFirst("x-b3-parentspanid"));
        return result;
    }
}
