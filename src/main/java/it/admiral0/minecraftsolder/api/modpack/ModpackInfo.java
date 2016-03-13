package it.admiral0.minecraftsolder.api.modpack;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLLog;
import it.admiral0.minecraftsolder.MinecraftConfig;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import it.admiral0.minecraftsolder.modpackbuilder.Utils;
import it.admiral0.minecraftsolder.pojo.pack.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.FileReader;
import java.nio.file.Files;

/**
 * Manages Modpack API URLS
 */
@Path("/modpack/")
public class ModpackInfo {
    @Inject
    private MinecraftConfig config;
    @Inject
    private Modpack pack;
    @Inject
    private Gson gson;
    private Logger log = FMLLog.getLogger();

    private static final String ICON_FILE = "icon.png";
    private static final String LOGO_FILE = "logo.png";
    private static final String BG_FILE = "background.png";

    @GET @Produces("application/json")
    public String getNoSlug() throws Exception{
        return gson.toJson(
                ModpackListObject.builder()
                        .modpack(config.getModpackName(),config.getModpackName())
                        .mirrorUrl(config.getMirrorUrl())
                .build()
        );
    }

    @GET @Path("{modpack}")  @Produces("application/json")
    public String getWithSlug(@PathParam("modpack") String modpack) throws Exception {
        if(!config.getModpackName().equals(modpack))
            return errorString();
        final java.nio.file.Path p = pack.getSolderCache();
        return gson.toJson(
                ModpackInfoObject.builder()
                        .name(config.getModpackName())
                        .displayName(config.getModpackName())
                        .url(null)
                        .icon((Files.exists(p.resolve("icon.png"))) ? "/icon.png" : null)
                        .icon_md5((Files.exists(p.resolve("icon.png"))) ? Utils.md5(p.resolve("icon.png").toFile()) : null)
                        .logo((Files.exists(p.resolve("logo.png"))) ? "/logo.png" : null)
                        .logo_md5((Files.exists(p.resolve("logo.png"))) ? Utils.md5(p.resolve("logo.png").toFile()) : null)
                        .background((Files.exists(p.resolve("background.png"))) ? "/background.png" : null)
                        .background_md5((Files.exists(p.resolve("background.png"))) ? Utils.md5(p.resolve("background.png").toFile()) : null)
                        .recommended(config.getModpackVersion())
                        .latest(config.getModpackVersion())
                        .builds(pack.getAllVersions())
                        .build()
        );
    }

    @GET @Path("{modpack}/{version}") @Produces("application/json")
    public String getModpackVersion(@PathParam("modpack") String modpack, @PathParam("version") String version) throws Exception{
        if(!config.getModpackName().equals(modpack))
            return errorString();
        if(!pack.getAllVersions().contains(version))
            return errorString();

        ModpackCacheObject cache = gson.fromJson(new FileReader(pack.getPackCache().resolve(version + ".json").toFile()), ModpackCacheObject.class);
        ModpackInfoVersionObject.ModpackInfoVersionObjectBuilder builder = ModpackInfoVersionObject.builder();
        builder.forge(cache.getForge())
                .javaArgs(cache.getJavaArgs())
                .memory(cache.getMemory())
                .minecraft(cache.getMinecraft());

        cache.getMods().entrySet().stream().forEach(
                entry -> {
                    try {
                        builder.mod(
                                SingleModObject.builder()
                                        .name(entry.getKey())
                                        .version(entry.getValue())
                                        .url(config.getMirrorUrl() + "/mods/" + entry.getKey() + "_" + entry.getValue() + ".zip")
                                        .md5(Utils.md5(pack.getModCache().resolve(entry.getKey() + "_" + entry.getValue() + ".zip").toFile()))
                                        .build()
                        );
                    } catch (Exception e){
                        log.error("Cannot calculate MD5 for mod " + entry.getKey() + " ver " + entry.getValue(), e);
                    }
                }
        );

        return gson.toJson(builder.build());
    }

    private String errorString() {
        return "{\n" +
                "  error: \"Modpack does not exist/Build does not exist\"\n" +
                "}";
    }
}
