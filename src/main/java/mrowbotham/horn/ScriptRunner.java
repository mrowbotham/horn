package mrowbotham.horn;

import mrowbotham.horn.dependencies.Javascript;
import org.mozilla.javascript.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScriptRunner {
    private final Map<String, Script> cache = new LinkedHashMap<String, Script>(1024) {
        @Override
        protected boolean removeEldestEntry(final Map.Entry<String, Script> eldest) {
            return super.size() > 1024;
        }
    };

    private Scriptable scope;
    private Context cx;

    public ScriptRunner init(Javascript... dependencies) throws IOException {
        cx = ContextFactory.getGlobal().enterContext();
        cx.setOptimizationLevel(-1);
        scope = cx.initStandardObjects();
        cx.evaluateString(scope, "var print = function(msg) { java.lang.System.out.println(msg); };", "print", 1, null);
        for (Javascript javascript : dependencies) {
            javascript.load(cx, scope);
        }
        return this;
    }

    public void destroy() {
        Context.exit();
    }

    public <T> T run(String function, Class<T> type, Object... args) {
        try {
            final Context localContext = Context.enter();
            final Scriptable threadScope = localContext.newObject(scope);
            threadScope.setPrototype(scope);
            threadScope.setParentScope(null);

            for (int i = 0; i < args.length; i++) {
                threadScope.put("arg" + i, threadScope, toJS(args[i], threadScope));
            }

            final Script script = getScript(function, args);
            return (T)Context.jsToJava(script.exec(localContext, threadScope), type);
        } finally {
            Context.exit();
        }
    }

    private Object toJS(Object arg, Scriptable scope) {
        if (arg instanceof Map) {
            final NativeObject nativeObject = new NativeObject();
            final Map<String, ?> map = (Map<String, ?>) arg;
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                nativeObject.defineProperty(entry.getKey(), entry.getValue(), NativeObject.READONLY);
            }
            return nativeObject;
        }
        return Context.javaToJS(arg, scope);
    }

    private Script getScript(String function, Object[] args) {
        String functionCallStart = function + "(";
        for (int i = 0; i < args.length; i++) {
            functionCallStart += "arg" + i + ",";
        }
        final String functionCall = functionCallStart.substring(0, functionCallStart.length() - 1) + ");";

        synchronized (cache) {
            if (cache.containsKey(functionCall)) {
                return cache.get(functionCall);
            }

            final Script script = cx.compileString(functionCall, functionCall, 1, null);
            cache.put(functionCall, script);
            return script;
        }
    }
}
