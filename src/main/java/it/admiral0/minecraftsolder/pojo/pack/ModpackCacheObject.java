package it.admiral0.minecraftsolder.pojo.pack;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.Map;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter
@Setter @Builder
public class ModpackCacheObject {
    @Singular private Map<String,String> mods;
    private String minecraft;
    private String forge;
    @SerializedName("java")
    private String javaArgs;
    private String memory;
}
