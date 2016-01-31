package it.admiral0.minecraftsolder.api.mod;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.StringWriter;

/**
 * Created by admiral0 on 30/01/16.
 */
@Path("/mod")
public class ModInfo {
    @Inject private Loader loader;

    @GET @Produces("application/json")
    public String getNoMod() throws Exception{
        StringWriter w = new StringWriter();
        JsonFactory f = new JsonFactory();
        JsonGenerator j = f.createGenerator(w);
        j.writeStartObject();
        j.writeObjectField("error", "No mod requested/Mod does not exist/Mod version does not exist");
        j.flush();
        return w.toString();
    }

    @Path("/{modid}")
    public String getModInfo(@PathParam("modid") String modid) throws Exception{
        ModContainer mod = null;
        for(ModContainer mc : loader.getModList()){
            if(mc.getMetadata().modId.equals(modid)){
                mod = mc;
            }
        }
        if(mod != null)
            return getNoMod();
        ModMetadata meta = mod.getMetadata();
        assert meta != null;
    }
}
