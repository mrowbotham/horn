package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import static mrowbotham.horn.dependencies.glob.Globber.globToRegex;
import static mrowbotham.horn.dependencies.glob.Lister.listRecursively;

public class FileGlob implements Javascript {
    private Pattern glob;
    private String baseDir;

    public FileGlob(String baseDir, String glob) {
        this.baseDir = baseDir;
        this.glob = globToRegex(glob);
    }

    @Override
    public void load(Context cx, Scriptable scope) throws IOException {
        for (String filePath : listRecursively(baseDir)) {
            if (glob.matcher(filePath).matches()) {
                final Reader reader = new FileReader(filePath);
                try {
                    cx.evaluateReader(scope, reader, filePath, 1, null);
                } finally {
                    reader.close();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileGlob fileGlob = (FileGlob) o;
        if (baseDir != null ? !baseDir.equals(fileGlob.baseDir) : fileGlob.baseDir != null) return false;
        return !(glob != null ? !glob.pattern().equals(fileGlob.glob.pattern()) : fileGlob.glob != null);
    }

    @Override
    public int hashCode() {
        int result = glob != null ? glob.hashCode() : 0;
        result = 31 * result + (baseDir != null ? baseDir.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileGlob{glob=" + glob + ", baseDir='" + baseDir + '\'' + '}';
    }
}
