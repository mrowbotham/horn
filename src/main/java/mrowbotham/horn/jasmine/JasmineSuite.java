package mrowbotham.horn.jasmine;

import mrowbotham.horn.ScriptRunner;
import mrowbotham.horn.dependencies.*;
import mrowbotham.horn.logging.ConsoleLogger;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JasmineSuite extends Runner {
    private final ScriptRunner runner;
    private final Description description;

    public JasmineSuite(Class testClass) {
        try {
            final WithJavascript withJavascriptAnnotation = (WithJavascript)testClass.getAnnotation(WithJavascript.class);
            final List<Javascript> dependencies = new ArrayList<>();
            dependencies.add(new EnvJs());
            dependencies.add(new Jasmine());
            for (String mainPath : withJavascriptAnnotation.srcFiles()) {
                dependencies.add(new FileGlob(withJavascriptAnnotation.srcDir(), mainPath));
            }
            for (String testPath : withJavascriptAnnotation.specFiles()) {
                dependencies.add(new FileGlob(withJavascriptAnnotation.specDir(), testPath));
            }
            runner = new ScriptRunner().init(new ConsoleLogger(), dependencies.toArray(new Javascript[dependencies.size()]));
            description = Description.createSuiteDescription(testClass);
            for (Description child : getChildren(runner.run("jasmine.getEnv().currentRunner().topLevelSuites", NativeArray.class))) {
                description.addChild(child);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        return description;
    }

    private List<Description> getChildren(NativeArray suitesOrSpecs) {
        List<Description> descriptions = new ArrayList<Description>((int) suitesOrSpecs.getLength());
        for (int i = 0; i < suitesOrSpecs.getLength(); i++) {
            NativeObject suite = (NativeObject) suitesOrSpecs.get(i, suitesOrSpecs);
            Description description = Description.createSuiteDescription((String) suite.get("description", suite));
            if (runner.hasFunction(suite, "children")) {
                List<Description> children = getChildren(runner.run(suite, "children", NativeArray.class));
                for (Description child : children) {
                    description.addChild(child);
                }
            }
            descriptions.add(description);
        }
        return descriptions;
    }


    @Override
    public void run(RunNotifier notifier) {
        runner.run("runJasmine", Object.class, new JasmineRunNotifier(notifier, description));
    }
}
