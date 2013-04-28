package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.*;

public class Classpath implements Javascript {
    private String name;

    public Classpath(String name) {
        this.name = name;
    }

    public void load(Context cx, Scriptable scope) throws IOException {
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new FileNotFoundException(name);
        }
        final Reader reader = new InputStreamReader(stream);
        try {
            cx.evaluateReader(scope, reader, name, 1, null);
        } finally {
            reader.close();
        }
    }
}
