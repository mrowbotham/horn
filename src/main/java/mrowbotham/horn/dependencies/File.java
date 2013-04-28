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
}
