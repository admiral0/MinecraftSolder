package it.admiral0.minecraftsolder.pojo.mod;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter @Setter @Builder
public class ModInfoObject {
    private String name;
    private String prettyName;
    private String author;
    private String description;
    private String link;
    private String donate;
    @Singular private List<String> versions;
}
