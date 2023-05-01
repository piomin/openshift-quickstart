package pl.redhat.samples.quarkus.insurance.client;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class RequestHeaderFactory implements ClientHeadersFactory {

    @Inject
    Logger log;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> inHeaders,
                                                 MultivaluedMap<String, String> outHeaders) {
        String version = inHeaders.getFirst("x-version");
        log.infof("Version Header: %s", version);
        String traceId = inHeaders.getFirst("x-b3-traceid");
        log.infof("Trace Header: %s", traceId);
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("X-Version", version);
        result.add("X-B3-TraceId", traceId);
        result.add("X-B3-SpanId", inHeaders.getFirst("x-b3-spanid"));
        result.add("X-B3-ParentSpanId", inHeaders.getFirst("x-b3-parentspanid"));
        return result;
    }
}
