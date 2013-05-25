package mrowbotham.horn.jasmine;

import org.junit.ComparisonFailure;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.Map;

public class JasmineRunNotifier {
    private RunNotifier notifier;
    private Map<String, Description> descriptionsByName;

    public JasmineRunNotifier(RunNotifier notifier, Map<String, Description> descriptionsByName) {
        this.notifier = notifier;
        this.descriptionsByName = descriptionsByName;
    }

    public void start(String description) {
        notifier.fireTestStarted(descriptionsByName.get(description));
    }

    public void ok(String description) {
        notifier.fireTestFinished(descriptionsByName.get(description));
    }

    public void ignore(String description) {
        notifier.fireTestIgnored(descriptionsByName.get(description));
    }

    public void fail(String description, String message) {
        notifier.fireTestFailure(new Failure(descriptionsByName.get(description), new Error(message)));
    }

    public void comparisonFail(String description, String expected, String actual, String message) {
        notifier.fireTestFailure(new Failure(descriptionsByName.get(description), new ComparisonFailure(message, expected, actual)));
    }
}

