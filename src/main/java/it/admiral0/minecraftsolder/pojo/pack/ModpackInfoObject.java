package it.admiral0.minecraftsolder.pojo.pack;

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
public class ModpackInfoObject {
    private String name;
    private String displayName;
    private String url;
    private String icon;
    private String icon_md5;
    private String logo;
    private String logo_md5;
    private String background;
    private String background_md5;
    private String recommended;
    private String latest;
    private List<String> builds;
}
