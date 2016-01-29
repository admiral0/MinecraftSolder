package it.admiral0.minecraftsolder;

import java.net.URI;

/**
 * Created by admiral0 on 29/01/16.
 */
public class MinecraftConfig {
    private URI baseUri;
    private int port;
    private String apiKey;

    public MinecraftConfig(URI baseUri, String apiKey, int port) {
        this.baseUri = baseUri;
        this.apiKey = apiKey;
        this.port = port;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
