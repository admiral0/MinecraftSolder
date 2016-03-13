/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.admiral0.minecraftsolder.api;

import com.google.gson.Gson;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.pojo.InvalidKeyObject;
import it.admiral0.minecraftsolder.pojo.ValidKeyObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author danielguarise
 */
@Path("/api/verify/")
public class VerifyKey {
    
    @Inject
    private MinecraftConfig config;

    @Inject
    private Gson gson;

    @GET @Path("{apikey}") @Produces("application/json")
    public String verify(@PathParam("apikey") String apiKey) throws Exception{
        if (apiKey!=null && apiKey.length()!=0) {
            if (config.getApiKey().equalsIgnoreCase(apiKey)) {
                return gson.toJson(ValidKeyObject.builder().valid("Key validated.").build());
            } else {
                return gson.toJson(InvalidKeyObject.builder().error("Invalid key provided.").build());
            }
        }
        return gson.toJson(InvalidKeyObject.builder().error("No API key provided.").build());
       }
    
    @GET @Produces("application/json")
    public String verify() throws Exception{
        return verify(null);
    }
    
}
