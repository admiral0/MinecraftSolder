package it.admiral0.minecraftsolder;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Mod(modid = MinecraftSolder.MODID, version = MinecraftSolder.VERSION)
public class MinecraftSolder
{
    public static final String MODID = "minecraftsolder";
    public static final String VERSION = "1.0";

    private static final String CAT_WEBSERVER = "WebServer";

    private int port;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        Property portProperty = config.get(CAT_WEBSERVER, "port", 8000);
        portProperty.comment = "The port the webserver runs on.\n"
                + "You should set this on a nonstandard port and use a proxy "
                + "to serve the api. It is mapped to /";
        port = portProperty.getInt();

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception
    {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
        ResourceConfig config = new ResourceConfig().packages("it.admiral0");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        server.start();
    }
}
