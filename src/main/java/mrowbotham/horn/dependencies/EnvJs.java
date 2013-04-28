package mrowbotham.horn.dependencies;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;

public class EnvJs implements Javascript {
    @Override
    public void load(Context cx, Scriptable scope) throws IOException {
        new Classpath("js/env.js/platform/core.js").load(cx, scope);
        new Classpath("js/env.js/platform/rhino.js").load(cx, scope);
        new Classpath("js/env.js/console.js").load(cx, scope);
        new Classpath("js/env.js/dom.js").load(cx, scope);
        new Classpath("js/env.js/event.js").load(cx, scope);
        new Classpath("js/env.js/html.js").load(cx, scope);
        new Classpath("js/env.js/css.js").load(cx, scope);
        new Classpath("js/env.js/parser.js").load(cx, scope);
        new Classpath("js/env.js/xhr.js").load(cx, scope);
        new Classpath("js/env.js/timer.js").load(cx, scope);
        new Classpath("js/env.js/window.js").load(cx, scope);
    }
}
