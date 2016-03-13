package it.admiral0.minecraftsolder.api;

import com.google.gson.Gson;
import it.admiral0.minecraftsolder.pojo.ApiVersionObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by admiral0 on 29/01/16.
 */
@Path("/api")
public class ApiVersion {
    @Inject
    private Gson gson;

    @GET @Produces("application/json")
    public String getVersion() throws Exception{
        return gson.toJson(ApiVersionObject.builder()
                .api("TechnicSolder")
                .version("v0.7.2.0")
                .stream("DEV")
                .extraver("0.2antani")
                .build());
    }
}
