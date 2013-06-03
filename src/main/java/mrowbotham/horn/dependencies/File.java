package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class File implements Javascript {
    private String path;

    public File(String path) {
        this.path = path;
    }

    @Override
    public void load(Context cx, Scriptable scope) throws IOException {
        final Reader reader = new FileReader(path);
        try {
            cx.evaluateReader(scope, reader, path, 1, null);
        } finally {
            reader.close();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return !(path != null ? !path.equals(file.path) : file.path != null);

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
