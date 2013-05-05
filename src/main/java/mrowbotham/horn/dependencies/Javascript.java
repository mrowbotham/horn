package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;

public interface Javascript {
    void load(Context cx, Scriptable scope) throws IOException;
}
