package it.admiral0.minecraftsolder;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import it.admiral0.minecraftsolder.modpackbuilder.Modpack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Mod(modid = MinecraftSolder.MODID, version = MinecraftSolder.VERSION, acceptableRemoteVersions = "*")
public class MinecraftSolder
{
    public static final String MODID = "minecraftsolder";
    public static final String VERSION = "0.4.8";

    private static final String CAT_WEBSERVER = "WebServer";
    private static final String CAT_TECHNIC = "Technic";
    private static final String CAT_LAUNCHER = "Launcher";
    private static final String CAT_GENERAL = "General";

    public static Logger logger;

    private Modpack modpack;

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .create();

    private MinecraftConfig solderConfig = new MinecraftConfig();


    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();


        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        Property enabledProp = config.get(CAT_GENERAL, "enabled", false);
        enabledProp.comment = "The mod is disabled by default. Change this to true to generate cache the next startup";
        solderConfig.setEnabled(enabledProp.getBoolean());

        Property portProperty = config.get(CAT_WEBSERVER, "port", 8000);
        portProperty.comment = "The port the webserver runs on.\n"
                + "You should set this on a nonstandard port and use a proxy "
                + "to serve the api. It is mapped to /";

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(portProperty.getInt()).build();
        solderConfig.setBaseUri(baseUri);

        Property apiKeyProperty = config.get(CAT_TECHNIC, "apiKey", "INVALID_KEY");
        apiKeyProperty.comment = "The apiKey on Technic Platform";
        solderConfig.setApiKey(apiKeyProperty.getString());

        Property modpackNameProperty = config.get(CAT_TECHNIC, "modpackName", "MyModpack");
        modpackNameProperty.comment = "The name of the modpack. Should be the same name as on technic platform";
        solderConfig.setModpackName( modpackNameProperty.getString());

        Property modpackVersionProperty = config.get(CAT_TECHNIC, "modpackVersion", "0.1");
        modpackVersionProperty.comment = "Version of the modpack. It's the current installed version";
        solderConfig.setModpackVersion(modpackVersionProperty.getString());

        Property mirrorUrlProperty = config.get(CAT_WEBSERVER, "mirrorUrl", "http://localhost:8000/download/");
        mirrorUrlProperty.comment = "The external URL to expose downloads";
        solderConfig.setMirrorUrl(mirrorUrlProperty.getString());

        Property javaArgs = config.get(CAT_LAUNCHER, "javaArgs", "");
        javaArgs.comment = "Java ARGS for the client";
        solderConfig.setJavaArgs(javaArgs.getString());

        Property javaMem = config.get(CAT_LAUNCHER, "javaMem", "1G");
        javaMem.comment  = "Java default Memory for the client";
        solderConfig.setJavaMem(javaMem.getString());

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception
    {
        if(event.getSide().isServer())
            modpack = new Modpack(logger, solderConfig, gson);
        if(event.getSide().isServer() && solderConfig.isEnabled()) {

            logger.info("Loading mod MinecraftSolder");
            ResourceConfig config = new ResourceConfig()
                    .packages("it.admiral0")
                    .register(new AbstractBinder() {
                        @Override
                        protected void configure() {
                            bind(solderConfig);
                            bind(Loader.instance());
                            bind(modpack);
                            bind(gson);
                        }
                    });
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(solderConfig.getBaseUri(), config);
            server.getServerConfiguration().addHttpHandler(
                    new StaticHttpHandler(modpack.getSolderCache().toAbsolutePath().toString()), "/download"
            );
            server.start();
            logger.info("Server running on " + solderConfig.getBaseUri().toString());
        }else{
            logger.info("Mod is disabled.");
        }
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) throws Exception {
        if(event.getSide().isServer() && solderConfig.isEnabled()){
            if(modpack.needsBuild()){
                modpack.build();
            }
        }
    }
}
