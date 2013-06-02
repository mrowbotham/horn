package mrowbotham.horn.jasmine;

import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class JasmineSuiteTest {
    @WithJavascript(srcDir = "src/test/resources", srcFiles = {"**/Player.js", "**/Song.js", "**/Faily.js"},
            specDir = "src/test/resources", specFiles = {"**/SpecHelper.js", "**/PlayerSpec.js", "**/FailySpec.js"})
    class ThreeSpecsClass {
    }

    @WithJavascript(srcDir = "src/test/resources", srcFiles = {"**/Player.js", "**/Song.js"},
            specDir = "src/test/resources", specFiles = {"**/SpecHelper.js", "**/PlayerSpec.js"})
    class HappySpecsClass {
    }

    @WithJavascript(srcDir = "src/test/resources", srcFiles = {"**/Faily.js"}, specDir = "src/test/resources", specFiles = {"**/FailySpec.js"})
    class SadSpecsClass {
    }

    @Mock RunNotifier runNotifier;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void canGetDescriptions() {
        final JasmineSuite bigSuite = new JasmineSuite(ThreeSpecsClass.class);
        final Description root = bigSuite.getDescription();
        assertEquals(ThreeSpecsClass.class.getName(), root.getDisplayName());
        assertEquals(3, root.getChildren().size());

        final Description player = root.getChildren().get(0);
        assertEquals("Player", player.getDisplayName());
        assertEquals(4, player.getChildren().size());
        assertEquals("should be able to play a Song", player.getChildren().get(0).getDisplayName());
        final Description paused = player.getChildren().get(1);
        assertEquals("when song has been paused", paused.getDisplayName());
        assertEquals(2, paused.getChildren().size());
        assertEquals("should indicate that the song is currently paused", paused.getChildren().get(0).getDisplayName());
        assertEquals("should be possible to resume", paused.getChildren().get(1).getDisplayName());
        assertEquals("tells the current song if the user has made it a favorite", player.getChildren().get(2).getDisplayName());
        final Description resume = player.getChildren().get(3);
        assertEquals("resume", resume.getDisplayName());
        assertEquals(1, resume.getChildren().size());
        assertEquals("should throw an exception if song is already playing", resume.getChildren().get(0).getDisplayName());

        final Description faily = root.getChildren().get(1);
        assertEquals(1, faily.getChildren().size());
        assertEquals("should not throw an error (but it does)", faily.getChildren().get(0).getDisplayName());

        final Description basicallyBad = root.getChildren().get(2);
        assertEquals(2, basicallyBad.getChildren().size());
        final Description tru = basicallyBad.getChildren().get(0);
        assertEquals("true", tru.getDisplayName());
        assertEquals(1, tru.getChildren().size());
        assertEquals("should be false (but it isn't)", tru.getChildren().get(0).getDisplayName());
        final Description one = basicallyBad.getChildren().get(1);
        assertEquals("1", one.getDisplayName());
        assertEquals(1, one.getChildren().size());
        assertEquals("should be 2 (but it isn't)", one.getChildren().get(0).getDisplayName());
    }

    @Test
    public void canRunHappyTests() {
        final JasmineSuite suite = new JasmineSuite(HappySpecsClass.class);

        suite.run(runNotifier);
        verifyStarted("should be able to play a Song");
        verifyStarted("should indicate that the song is currently paused");
        verifyStarted("should be possible to resume");
        verifyStarted("tells the current song if the user has made it a favorite");
        verifyStarted("should throw an exception if song is already playing");
        verifyFinished("should be able to play a Song");
        verifyFinished("should indicate that the song is currently paused");
        verifyFinished("should be possible to resume");
        verifyFinished("tells the current song if the user has made it a favorite");
        verifyFinished("should throw an exception if song is already playing");
    }

    @Test
    public void canRunSadTests() {
        final JasmineSuite suite = new JasmineSuite(SadSpecsClass.class);

        suite.run(runNotifier);
        verifyStarted("should not throw an error (but it does)");
        verifyStarted("should be false (but it isn't)");
        verifyStarted("should be 2 (but it isn't)");
        verifyFailed("should not throw an error (but it does)", "Faily.js:4");
        verifyFailed("should be false (but it isn't)", "FailySpec.js:16", "true", "undefined");
        verifyFailed("should be 2 (but it isn't)", "FailySpec.js:21", "1", "2");
    }

    private void verifyStarted(String name) {
        verify(runNotifier).fireTestStarted(Description.createSuiteDescription(name));
    }

    private void verifyFinished(String name) {
        verify(runNotifier).fireTestFinished(Description.createSuiteDescription(name));
    }

    private void verifyFailed(String name, String stackTraceContains) {
        verify(runNotifier).fireTestFailure(argThat(failedWithError(name, stackTraceContains)));
    }

    private void verifyFailed(String name, String stackTraceContains, String actual, String expceted) {
        verify(runNotifier).fireTestFailure(argThat(failedWithComparisonError(name, stackTraceContains, actual, expceted)));
    }

    private ArgumentMatcher<Failure> failedWithError(final String name, final String stackTraceContains) {
        return new ArgumentMatcher<Failure>() {
            public boolean matches(Object failure) {
                return name.equals(((Failure) failure).getDescription().getDisplayName()) &&
                        ((Failure) failure).getException().getMessage().contains(stackTraceContains);
            }
        };
    }

    private ArgumentMatcher<Failure> failedWithComparisonError(final String name, final String stackTraceContains,
                                                               final String actual, final String expected) {
        return new ArgumentMatcher<Failure>() {
            public boolean matches(Object failure) {
                if (!(((Failure) failure).getException() instanceof ComparisonFailure)) {
                    return false;
                }
                ComparisonFailure comparisonFailure = (ComparisonFailure)((Failure) failure).getException();
                return name.equals(((Failure) failure).getDescription().getDisplayName()) &&
                        comparisonFailure.getMessage().contains(stackTraceContains) &&
                        comparisonFailure.getActual().equals(actual) && comparisonFailure.getExpected().equals(expected);
            }
        };
    }
}
