package mrowbotham.horn;

import mrowbotham.horn.dependencies.Classpath;
import mrowbotham.horn.dependencies.EnvJs;
import mrowbotham.horn.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoggingTest {
    @Mock Logger logger;
    private ScriptRunner runner;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        runner = new ScriptRunner().init(logger, new EnvJs(), new Classpath("js/test-script-logging.js"));
    }

    @After
    public void tearDown() {
        runner.destroy();
    }

    @Test
    public void sendsPrintStatementsToTheLog() throws Exception {
        runner.run("doPrint", Object.class, "message for you");

        verify(logger).log("message for you");
    }

    @Test
    public void sendsConsoleLogStatementsToTheLog() throws Exception {
        runner.run("doConsoleLog", Object.class, "message for you");

        verify(logger).log("message for you");
    }
}
