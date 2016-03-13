package it.admiral0.minecraftsolder.pojo.pack;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter
@Setter @Builder
public class ModpackInfoVersionObject {
    private String minecraft;
    private String forge;
    @SerializedName("java")
    private String javaArgs;
    private String memory;
    @Singular private List<SingleModObject> mods;
    /*
    j.writeObjectField("minecraft", mc.get("minecraft"));
        j.writeObjectField("forge", mc.get("forge"));
        j.writeObjectField("java", mc.get("java"));
        j.writeObjectField("memory", mc.get("memory"));
        j.writeArrayFieldStart("mods");
        Map<String, String> mods = (Map<String, String>) mc.get("mods");
        for(String mod : mods.keySet()){
            j.writeStartObject();
            j.writeObjectField("name", mod);
            j.writeObjectField("version", mods.get(mod));
            j.writeObjectField("url", config.getMirrorUrl() + "/mods/" + mod + "_" + mods.get(mod) + ".zip");
            j.writeObjectField("md5", Utils.md5(pack.getModCache().resolve(mod+ "_" + mods.get(mod) + ".zip").toFile()));
            j.writeEndObject();
        }
        j.writeEndArray();
        j.writeEndObject();
        j.flush();
        j.close();
     */
}
