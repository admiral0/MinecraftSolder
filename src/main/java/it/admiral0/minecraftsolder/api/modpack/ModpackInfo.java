package it.admiral0.minecraftsolder.api.modpack;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import it.admiral0.minecraftsolder.modpackbuilder.Utils;
import scala.actors.threadpool.Arrays;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

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

    @GET @Path("{modpack}")  @Produces("application/json")
    public String getWithSlug(@PathParam("modpack") String modpack) throws Exception {
        if(!config.getModpackName().equals(modpack))
            return errorString();
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
        j.writeArrayFieldStart("builds");
        pack.getAllVersions().stream().forEach((s) -> {
            try {
                j.writeString(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        j.writeEndArray();
        j.flush();
        j.close();
        return w.toString();
    }

    @GET @Path("{modpack}/{version}") @Produces("application/json")
    public String getModpackVersion(@PathParam("modpack") String modpack, @PathParam("version") String version) throws Exception{
        if(!config.getModpackName().equals(modpack))
            return errorString();
        if(!pack.getAllVersions().contains(version))
            return errorString();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mc = mapper.readValue(pack.getPackCache().resolve(version + ".json").toFile(), Map.class);

        StringWriter w = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(w);
        j.writeStartObject();

        j.writeObjectField("minecraft", mc.get("minecraft"));
        j.writeObjectField("forge", mc.get("forge"));
        j.writeObjectField("java", mc.get("java"));
        j.writeObjectField("memory", mc.get("memory"));
        j.writeArrayFieldStart("mods");
        Map<String, String> mods = (Map<String, String>) mc.get("mods");
        for(String mod : mods.keySet()){
            j.writeStartObject();
            j.writeObjectField("name", mod);
            j.writeObjectField("version", mods.get(mod));
            j.writeObjectField("url", config.getMirrorUrl() + "/mods/" + mod + "_" + mods.get(mod) + ".zip");
            j.writeObjectField("md5", Utils.md5(pack.getModCache().resolve(mod+ "_" + mods.get(mod) + ".zip").toFile()));
            j.writeEndObject();
        }
        j.writeEndArray();
        j.writeEndObject();
        j.flush();
        j.close();
        return w.toString();
    }

    private String errorString() {
        return "{\n" +
                "  error: \"Modpack does not exist/Build does not exist\"\n" +
                "}";
    }


    private void populateIcons(JsonGenerator j) throws Exception{
        final java.nio.file.Path p = pack.getSolderCache();
        List<String> imgs = Lists.newArrayList("icon", "logo", "background");
        imgs.stream().forEach( img -> {
            try {
                if(Files.exists(p.resolve(img + ".png"))){
                    j.writeObjectField(img, "/" + img + ".png");
                    j.writeObjectField(img + "_md5",Utils.md5(p.resolve(img + ".png").toFile()));
                }else{
                    j.writeObjectField(img, null);
                    j.writeObjectField(img + "_md5", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
