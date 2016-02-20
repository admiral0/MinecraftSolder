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
}
