package mrowbotham.horn.dependencies.glob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lister {
    public static List<String> listRecursively(String baseDir) throws IOException {
        File root = new File(baseDir);
        List<String> files = new ArrayList<String>();
        recurse(root, files);
        return files;
    }

    private static void recurse(File root, List<String> files) throws IOException {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                recurse(file, files);
            } else {
                files.add(unPwd(file.getPath()));
            }
        }
    }

    private static String unPwd(String path) {
        return path.startsWith("./") ? path.substring(2) : path;
    }
}
