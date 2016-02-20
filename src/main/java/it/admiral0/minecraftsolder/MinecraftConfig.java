package it.admiral0.minecraftsolder;

import java.net.URI;

/**
 * Created by admiral0 on 29/01/16.
 */
public class MinecraftConfig {
    private URI baseUri;
    private int port;
    private String apiKey;
    private String modpackName;
    private String modpackVersion;
    private String mirrorUrl;
    private String javaArgs;
    private String javaMem;

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

    public String getModpackName() {
        return modpackName;
    }

    public void setModpackName(String modpackName) {
        this.modpackName = modpackName;
    }

    public String getModpackVersion() {
        return modpackVersion;
    }

    public void setModpackVersion(String modpackVersion) {
        this.modpackVersion = modpackVersion;
    }

    public String getMirrorUrl() {
        return mirrorUrl;
    }

    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    public String getJavaArgs() {
        return javaArgs;
    }

    public void setJavaArgs(String javaArgs) {
        this.javaArgs = javaArgs;
    }

    public String getJavaMem() {
        return javaMem;
    }

    public void setJavaMem(String javaMem) {
        this.javaMem = javaMem;
    }
}
