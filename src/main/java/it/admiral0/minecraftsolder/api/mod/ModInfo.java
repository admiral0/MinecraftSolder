package it.admiral0.minecraftsolder.api.mod;

import com.google.gson.Gson;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.MinecraftSolder;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import it.admiral0.minecraftsolder.modpackbuilder.Utils;
import it.admiral0.minecraftsolder.pojo.mod.ErrorObject;
import it.admiral0.minecraftsolder.pojo.mod.ModInfoObject;
import it.admiral0.minecraftsolder.pojo.mod.ModInfoVersionObject;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.nio.file.Files;
import java.util.stream.Collectors;

/**
 * This class exposes the API for MOD listing
 */
@Path("/mod")
public class ModInfo {
    @Inject private Loader loader;
    @Inject private Modpack pack;
    @Inject private MinecraftConfig config;
    @Inject private Gson gson;
    private Logger logger = MinecraftSolder.logger;

    @GET @Produces("application/json")
    public String getNoMod() throws Exception{
        return gson.toJson(
            ErrorObject.builder()
                    .error("No mod requested/Mod does not exist/Mod version does not exist")
                    .build()
        );
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

        return gson.toJson(
                ModInfoObject.builder()
                        .name(meta.modId)
                        .prettyName(meta.name)
                        .author(meta.authorList.stream().collect(Collectors.joining(",")))
                        .description(meta.description)
                        .link(meta.url)
                        .donate(null)
                        .versions(
                                Files.list(pack.getModCache())
                                        .map(java.nio.file.Path::getFileName)
                                        .filter(in -> in != null)
                                        .map(java.nio.file.Path::toString)
                                        .filter( fn -> fn.startsWith(meta.modId) && fn.endsWith(".zip") )
                                        .filter( fn -> fn.startsWith(meta.modId) && fn.endsWith(".zip") )
                                        .map(in -> in.toString()
                                                .replaceAll(meta.modId + "_", "")
                                                .replaceAll(".zip","")
                                        )
                                        .collect(Collectors.toList())
                        )
                .build()
        );
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
        return gson.toJson(
                ModInfoVersionObject.builder()
                        .url(config.getMirrorUrl() + "/mods/" + modid + "_" + version + ".zip")
                        .md5( Utils.md5(pack.getModCache().resolve(modid+"_"+ version+".zip").toFile()))
                .build()
        );
    }
}
