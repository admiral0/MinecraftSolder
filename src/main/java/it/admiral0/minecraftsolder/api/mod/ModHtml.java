package it.admiral0.minecraftsolder.api.mod;

import cpw.mods.fml.common.Loader;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by admiral0 on 25/02/16.
 */
@Path("mods.html")
public class ModHtml {
    @Inject
    private Loader loader;
    @GET @Produces("text/html")
    public String getModList(){
        StringBuilder out = new StringBuilder();
        out
                .append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head><title>Mod List</title></head>")
                .append("<body><table>\n");

        loader.getModList().stream().forEach(
                p-> out
                        .append("<tr>\n<td>\n")
                        .append(p.getModId())
                        .append("\n</td><td>\n")
                        .append(p.getVersion())
                        .append("\n</td></tr>")
        );
        out.append("</body></html>");
        return out.toString();
    }
}
