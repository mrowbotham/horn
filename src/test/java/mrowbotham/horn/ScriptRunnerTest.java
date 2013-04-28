package mrowbotham.horn;


import mrowbotham.horn.dependencies.Classpath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScriptRunnerTest {
    private ScriptRunner runner;

    @Before
    public void setUp() throws Exception {
        runner = new ScriptRunner().init(new Classpath("js/test-script-simple.js"));
    }

    @After
    public void tearDown() {
        runner.destroy();
    }

    @Test
    public void canRunSimpleScriptWithIntegers() throws Exception {
        final int result = runner.run("add", Integer.class, 2, 3);

        assertEquals(5, result);
    }

    @Test
    public void canRunSimpleScriptWithStrings() throws Exception {
        final String result = runner.run("add", String.class, "Hello", "World");

        assertEquals("HelloWorld", result);
    }

}
