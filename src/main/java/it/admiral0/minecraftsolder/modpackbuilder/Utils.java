package it.admiral0.minecraftsolder.modpackbuilder;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;

/**
 * Created by admiral0 on 20/02/16.
 */
public class Utils {
    public static String md5(File file) throws Exception {
        return Files.hash(file, Hashing.md5()).toString();
    }

    public static String sanitizeVersion(String inVer) {
        return inVer.replaceAll(" ","");
    }

    public static String sanitizeName(String inName) {
        return inName.replaceAll("\\|", "");
    }

    public static String getFilename(String name, String version) {
        return sanitizeName(name) + "_" + sanitizeVersion(version);
    }
}
