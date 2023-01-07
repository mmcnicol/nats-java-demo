package com.mycompany.nats.java.demo;

import io.nats.client.Connection;
import io.nats.client.Nats;
import java.nio.charset.StandardCharsets;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.jboss.logging.Logger;

@Path("v1")
public class MessageResource {

    private final Logger log = Logger.getLogger(MessageResource.class);

    @GET
    @Path("/queue/send2")
    @Produces("application/json")
    public Response queueSend2() {
        log.info("MessageResource in queueSend2()");
        try {
            String url = "nats://nats:4222";
            Connection natsConnection = Nats.connect(url); // localhost on port 4222
            natsConnection.publish("foo.bar", "Hi there!".getBytes(StandardCharsets.UTF_8));
            natsConnection.publish("foo.bar", "test123", "Hi there!".getBytes(StandardCharsets.UTF_8));
            
            ResponseBuilder response = Response.ok();
            return response.build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally {
        }
    }

}
