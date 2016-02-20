package it.admiral0.minecraftsolder.api.mod;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.MinecraftSolder;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import it.admiral0.minecraftsolder.modpackbuilder.Utils;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by admiral0 on 30/01/16.
 */
@Path("/mod")
public class ModInfo {
    @Inject private Loader loader;
    @Inject private Modpack pack;
    @Inject private MinecraftConfig config;
    private Logger logger = MinecraftSolder.logger;

    @GET @Produces("application/json")
    public String getNoMod() throws Exception{
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField("error", "No mod requested/Mod does not exist/Mod version does not exist");
        j.writeEndObject();
        j.flush();
        return w.toString();
    }

    @Path("{modid}") @GET @Produces("application/json")
    public String getModInfo(@PathParam("modid") String modid) throws Exception{
        ModContainer mod = null;
        for(ModContainer mc : loader.getModList()){
            if(mc.getMetadata().modId.equals(modid)){
                mod = mc;
            }
        }
        if(mod == null)
            return getNoMod();
        ModMetadata meta = mod.getMetadata();
        assert meta != null;
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField("name", meta.modId);
        j.writeObjectField("pretty_name", meta.name);
        j.writeObjectField("author", meta.authorList.stream().collect(Collectors.joining(",")));
        j.writeObjectField("description", meta.description);
        j.writeObjectField("link",meta.url);
        j.writeObjectField("donate", null);
        j.writeArrayFieldStart("versions");
        j.writeObject(meta.version);
        j.writeEndArray();
        j.writeEndObject();
        j.flush();
        return w.toString();
    }

    @Path("{modid}/{version}") @GET @Produces("application/json")
    public String getModInfo(@PathParam("modid") String modid, @PathParam("version") String version) throws Exception{
        ModContainer mod = null;
        for(ModContainer mc : loader.getModList()){
            if(mc.getMetadata().modId.equals(modid)){
                mod = mc;
            }
        }
        if(mod == null)
            return getNoMod();
        ModMetadata meta = mod.getMetadata();
        assert meta != null;
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField("md5", Utils.md5(pack.getModCache().resolve(modid+"_"+ version+".zip").toFile()));
        j.writeObjectField("url", config.getMirrorUrl() + "/mods/" + modid + "_" + version + ".zip");
        j.writeEndObject();
        j.flush();
        return w.toString();
    }


}
