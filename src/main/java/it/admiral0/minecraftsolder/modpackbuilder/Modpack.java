package it.admiral0.minecraftsolder.modpackbuilder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import it.admiral0.minecraftsolder.MinecraftConfig;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by admiral0 on 19/02/16.
 */
public class Modpack {
    private static final String CACHE_DIR = "soldercache";
    private static final String CACHE_MOD_DIR = "mods";
    private static final String CACHE_PACK_DIR = "modpack";
    private static final String CACHE_VERSION_FILE = "cached_version.txt";

    private final Path solderCache;
    private final Path modCache;
    private final Path packCache;

    private final Logger logger;

    private final MinecraftConfig config;

    private final List<String> skipMods = Arrays.asList("mcp","FML");

    public Modpack(Logger logger, MinecraftConfig config) throws Exception {
        this.config = config;
        this.logger = logger;
        solderCache = Paths.get(Loader.instance().getConfigDir().getParent(), CACHE_DIR);
        if(!Files.exists(solderCache)){
            Files.createDirectories(solderCache);
        }
        modCache = solderCache.resolve(CACHE_MOD_DIR);
        if(!Files.exists(modCache))
            Files.createDirectories(modCache);

        packCache = solderCache.resolve(CACHE_PACK_DIR);
        if(!Files.exists(packCache))
            Files.createDirectories(packCache);
    }

    public boolean needsBuild() throws Exception{
        Path currentCache = solderCache.resolve(CACHE_VERSION_FILE);
        if(!Files.exists(currentCache)){
            Files.createFile(currentCache);
            return false;
        }
        String v = new String(Files.readAllBytes(currentCache));
        return ! config.getModpackVersion().equals(v);
    }


    public void build() throws Exception {
        ArrayList<File> loaded = new ArrayList<>();
        FileOutputStream fos = new FileOutputStream(packCache.toAbsolutePath().toString() + File.separator + config.getModpackVersion() + ".json");
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(fos);
        j.writeStartObject();
        j.writeObjectFieldStart("mods");
        j.writeStartObject();
        for(ModContainer mod : Loader.instance().getModList()){
            logger.info("MOD : " + mod.getModId());
            if(loaded.contains(mod.getSource()) || skipMods.contains(mod.getModId()) || mod.getSource().isDirectory())
                continue;

            packMod(mod);
            j.writeObjectField(mod.getModId(), mod.getVersion());
            loaded.add(mod.getSource());
        }
        j.writeObjectField(config.getModpackName()+"Config", config.getModpackVersion());
        packConfig();
        j.writeEndObject();
        j.writeEndObject();
        j.flush();
        j.close();
        fos.close();
        Files.write(solderCache.resolve(CACHE_VERSION_FILE), config.getModpackVersion().getBytes());
    }

    private void packMod(ModContainer mod) throws Exception{
        String modPath = modCache.toAbsolutePath().toString()
                + File.separator
                + mod.getModId() + "_" + mod.getVersion();
        if(new File(modPath + ".zip").exists())
            return;
        FileOutputStream fos = new FileOutputStream(modPath + ".zip");
        ZipOutputStream zos = new ZipOutputStream(fos);
        File src = mod.getSource();
        String fname = "mods/" + src.getName();
        if("Forge".equals(mod.getModId()))
            fname = "bin/modpack.jar";
        zos.putNextEntry(new ZipEntry(fname));
        Files.copy(src.toPath(), zos);
        zos.closeEntry();
        zos.close();
        fos.close();
        FileOutputStream json = new FileOutputStream(modPath + ".json");
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(json);
        j.writeStartObject();
        j.writeObjectField("modid", mod.getModId());
        j.writeObjectField("pretty_name", mod.getName());
        j.writeObjectField("author", mod.getMetadata().authorList.stream().collect(Collectors.joining(",")));
        j.writeObjectField("description", mod.getMetadata().description);
        j.writeObjectField("link", mod.getMetadata().url);
        j.writeObjectField("donate", null);
        j.writeObjectField("md5", Utils.md5(new File(modPath + ".zip")));
        j.writeEndObject();
        j.flush();
        j.close();
        json.close();
    }

    private void packConfig() throws Exception{
        String modPath = modCache.toAbsolutePath().toString() + File.separator + config.getModpackName() + "Config_" + config.getModpackVersion();
        FileOutputStream fos = new FileOutputStream(modPath + ".zip");
        ZipOutputStream zos = new ZipOutputStream(fos);
        Files.walk(solderCache.resolveSibling("config")).filter(Files::isRegularFile).forEach(p -> {
            try {
                zos.putNextEntry(new ZipEntry("config/" + p.toFile().getName()));
                Files.copy(p,zos);
                zos.closeEntry();
            } catch (IOException e) {
                logger.error("Cannot zip " + p.toString(), e);
            }
        });
        zos.close();
        fos.close();
        FileOutputStream json = new FileOutputStream(modPath + ".json");
        JsonFactory factory = new JsonFactory();
        JsonGenerator j = factory.createGenerator(json);
        j.writeStartObject();
        j.writeObjectField("modid", config.getModpackName() + "Config");
        j.writeObjectField("pretty_name", config.getModpackName() + " Config");
        j.writeObjectField("author", "MinecraftSolder");
        j.writeObjectField("description", "Modpack config for " + config.getModpackName());
        j.writeObjectField("link", "http://"); //TODO proper site
        j.writeObjectField("donate", "http://");
        j.writeObjectField("md5", Utils.md5(new File(modPath + ".zip")));
        j.writeEndObject();
        j.flush();
        j.close();
        json.close();
    }

    public String getInstalledModpack() throws IOException {
        return new String(Files.readAllBytes(solderCache.resolve(CACHE_VERSION_FILE)));
    }

    public List<String> getAllVersions() throws IOException {
        final ArrayList<String> versions= new ArrayList<>();
        Files.walk(packCache).filter(p -> p.endsWith(".json")).forEach(p -> {
            versions.add(p.getFileName().toString().replace(".json", ""));
        });
        return versions;
    }

    public Path getSolderCache() {
        return solderCache;
    }




}
