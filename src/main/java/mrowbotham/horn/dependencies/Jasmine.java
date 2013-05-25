package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;

public class Jasmine implements Javascript {
    @Override
    public void load(Context cx, Scriptable scope) throws IOException {
        new Classpath("js/jasmine/jasmine.js").load(cx, scope);
        new Classpath("js/jasmine/junit_direct_reporter.js").load(cx, scope);
        new Classpath("js/jasmine/run.js").load(cx, scope);
    }
}
