package it.admiral0.minecraftsolder.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by admiral0 on 13/03/16.
 */
@Getter
@Setter
@Builder
public class ApiVersionObject {
    private String api;
    private String version;
    private String stream;
    private String extraver;
}
