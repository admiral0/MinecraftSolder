package it.admiral0.minecraftsolder.pojo.pack;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter
@Setter
@Builder
public class SingleModObject {
    private String name;
    private String version;
    private String md5;
    private String url;
}
