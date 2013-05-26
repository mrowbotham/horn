package mrowbotham.horn.jasmine;

import org.junit.ComparisonFailure;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mozilla.javascript.NativeArray;

import java.util.Map;
import java.util.NoSuchElementException;

public class JasmineRunNotifier {
    private RunNotifier notifier;
    private Description description;

    public JasmineRunNotifier(RunNotifier notifier, Description description) {
        this.notifier = notifier;
        this.description = description;
    }

    public void start(NativeArray names) {
        notifier.fireTestStarted(getDescription(names));
    }

    public void ok(NativeArray names) {
        notifier.fireTestFinished(getDescription(names));
    }

    public void ignore(NativeArray names) {
        notifier.fireTestIgnored(getDescription(names));
    }

    public void fail(NativeArray names, String message) {
        notifier.fireTestFailure(new Failure(getDescription(names), new Error(message)));
    }

    public void comparisonFail(NativeArray names, String expected, String actual, String message) {
        notifier.fireTestFailure(new Failure(getDescription(names), new ComparisonFailure(message, expected, actual)));
    }

    private Description getDescription(NativeArray names) {
        Description pointer = description;
        for (Object id : names.getIds()) {
            pointer = getChildWithName(pointer, (String)names.get((int)id, names));
        }
        return pointer;
    }

    private Description getChildWithName(Description description, String name) {
        for (Description child : description.getChildren()) {
            if (name.equals(child.getDisplayName())) {
                return child;
            }
        }
        throw new NoSuchElementException("Child with name '" + name + "' not found");
    }
}

