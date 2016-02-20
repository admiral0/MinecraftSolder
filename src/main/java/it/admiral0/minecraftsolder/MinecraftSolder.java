package it.admiral0.minecraftsolder;

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
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Mod(modid = MinecraftSolder.MODID, version = MinecraftSolder.VERSION, acceptableRemoteVersions = "*")
public class MinecraftSolder
{
    public static final String MODID = "minecraftsolder";
    public static final String VERSION = "1.0";

    private static final String CAT_WEBSERVER = "WebServer";
    private static final String CAT_TECHNIC = "Technic";
    private static final String CAT_LAUNCHER = "Launcher";

    public static Logger logger;

    private Modpack modpack;

    private MinecraftConfig solderConfig = new MinecraftConfig();


    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();


        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

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

        Property modpackVersionProperty = config.get(CAT_TECHNIC, "modpackVersion", "1.0");
        modpackNameProperty.comment = "Version of the modpack. It's the current installed version";
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
        if(event.getSide().isServer()) {
            modpack = new Modpack(logger, solderConfig);
            logger.info("Loading mod MinecraftSolder");
            ResourceConfig config = new ResourceConfig()
                    .packages("it.admiral0")
                    .register(JacksonFeature.class)
                    .register(new AbstractBinder() {
                        @Override
                        protected void configure() {
                            bind(solderConfig);
                            bind(Loader.instance());
                            bind(modpack);
                        }
                    });
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(solderConfig.getBaseUri(), config);
            server.start();
            logger.info("Server running on " + solderConfig.getBaseUri().toString());
        }else{
            logger.info("Will not load if not on the client.");
        }
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) throws Exception {
        if(event.getSide().isServer()){
            if(modpack.needsBuild()){
                modpack.build();
            }
        }
    }
}
