package it.admiral0.minecraftsolder.api.modpack;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import it.admiral0.minecraftsolder.modpackbuilder.Utils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.StringWriter;

/**
 * Created by admiral0 on 20/02/16.
 */
@Path("/modpack/")
public class ModpackInfo {
    @Inject
    private MinecraftConfig config;
    @Inject
    private Modpack pack;

    private static final String ICON_FILE = "icon.png";
    private static final String LOGO_FILE = "logo.png";
    private static final String BG_FILE = "background.png";

    @GET @Produces("application/json")
    public String getNoSlug() throws Exception{
        StringWriter w = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(w);

        j.writeStartObject();
        j.writeObjectFieldStart("modpacks");
        j.writeStartObject();
        j.writeObjectField(config.getModpackName(), config.getModpackName());
        j.writeEndObject();
        j.writeObjectField("mirror_url", config.getMirrorUrl());
        j.flush();
        j.close();
        return w.toString();
    }

    @Path("/{modpack}") @GET @Produces("application/json")
    public String getWithSlug() throws Exception {
        StringWriter w = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(w);

        j.writeStartObject();

        j.writeObjectField("name", config.getModpackName());
        j.writeObjectField("display_name", config.getModpackName());
        j.writeObjectField("url", null);
        populateIcons(j);
        j.writeObjectField("recommended", config.getModpackVersion());
        j.writeObjectField("latest", config.getModpackVersion());
        j.writeObjectField("builds", pack.getAllVersions());
        j.flush();
        j.close();
        return w.toString();
    }


    private void populateIcons(JsonGenerator j) throws Exception{
        j.writeObjectField("icon", null);
        j.writeObjectField("icon_md5", null);
        j.writeObjectField("logo", null);
        j.writeObjectField("logo_md5", null);
        j.writeObjectField("background", null);
        j.writeObjectField("background_md5", null);
    }
}
