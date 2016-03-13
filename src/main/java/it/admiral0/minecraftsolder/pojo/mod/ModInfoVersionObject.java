package it.admiral0.minecraftsolder.pojo.mod;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter
@Setter
@Builder
public class ModInfoVersionObject {
    private String md5;
    private String url;
}
