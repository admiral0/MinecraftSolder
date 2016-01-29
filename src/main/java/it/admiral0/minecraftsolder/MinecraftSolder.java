package it.admiral0.minecraftsolder;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Mod(modid = MinecraftSolder.MODID, version = MinecraftSolder.VERSION, acceptableRemoteVersions = "*")
public class MinecraftSolder
{
    public static final String MODID = "minecraftsolder";
    public static final String VERSION = "1.0";

    private static final String CAT_WEBSERVER = "WebServer";
    private static final String CAT_TECHNIC = "Technic";

    public static Logger logger;

    private int port;
    private String apiKey;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        Property portProperty = config.get(CAT_WEBSERVER, "port", 8000);
        portProperty.comment = "The port the webserver runs on.\n"
                + "You should set this on a nonstandard port and use a proxy "
                + "to serve the api. It is mapped to /";
        port = portProperty.getInt();

        Property apiKeyProperty = config.get(CAT_TECHNIC, "apiKey", "INVALID_KEY");
        apiKeyProperty.comment = "The apiKey on Technic Platform";
        apiKey = apiKeyProperty.getString();

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception
    {
        if(event.getSide().isServer()) {

            logger.info("Loading mod MinecraftSolder");
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
            final MinecraftConfig mconf = new MinecraftConfig(baseUri,apiKey, port);
            ResourceConfig config = new ResourceConfig()
                    .packages("it.admiral0")
                    .register(JacksonFeature.class)
                    .register(new AbstractBinder() {
                        @Override
                        protected void configure() {
                            bind(mconf);
                        }
                    });
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
            server.start();
            logger.info("Server running on " + baseUri.toString());
        }else{
            logger.info("Will not load if not on the client.");
        }
    }
}
