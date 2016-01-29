/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.admiral0.minecraftsolder.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import it.admiral0.minecraftsolder.MinecraftConfig;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author danielguarise
 */
@Path("/verify/")
public class VerifyKey {
    
    @Inject
    private MinecraftConfig config;
    
    @GET @Path("{apikey}") @Produces("application/json")
    public String verify(@PathParam("apikey") String apiKey) throws Exception{
        String jsonKey = "error";
        String msg = "No API key provided.";
        
        if (apiKey!=null && apiKey.length()!=0) {
            
            if (config.getApiKey().equalsIgnoreCase(apiKey)) {
                jsonKey = "valid";
                msg = "Key validated.";
            } else {
                msg = "Invalid key provided.";
            }
            
        }
        
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField(jsonKey, msg);
        j.writeEndObject();
        j.flush();
        return w.toString();
    }
    
    @GET @Produces("application/json")
    public String verify() throws Exception{
        return verify(null);
    }
    
}
