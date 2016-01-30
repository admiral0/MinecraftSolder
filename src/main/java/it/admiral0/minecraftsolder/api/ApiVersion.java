package it.admiral0.minecraftsolder.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.StringWriter;

/**
 * Created by admiral0 on 29/01/16.
 */
@Path("/")
public class ApiVersion {
    @GET @Produces("application/json")
    public String getVersion() throws Exception{
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField("api","TechnicSolder");
        j.writeObjectField("version", "v0.7.2.0");
        j.writeObjectField("stream", "DEV");
        j.writeObjectField("extraver", "0.2antani");
        j.writeEndObject();
        j.flush();
        return w.toString();
    }
}
