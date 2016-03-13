package it.admiral0.minecraftsolder.pojo.pack;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter @Setter @Builder
public class ModpackInfoSingleObject {
    private String displayName;
    private String name;
    private String logo;
    private String logo_md5;
    private String icon;
    private String icon_md5;
    private String background;
    private String background_md5;
    @SerializedName("builds")
    private List<String> buildVersions;
    private String recommended;
    private String latest;
}
