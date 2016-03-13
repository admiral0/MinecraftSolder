package it.admiral0.minecraftsolder.pojo.pack;

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
public class ModpackListFullObject {
    @Singular private Map<String,ModpackInfoSingleObject> modpacks;
    private String mirrorUrl;
}
