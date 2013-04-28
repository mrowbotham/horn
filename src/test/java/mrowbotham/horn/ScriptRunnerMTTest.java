package mrowbotham.horn;


import mrowbotham.horn.dependencies.Classpath;
import mrowbotham.horn.dependencies.EnvJs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScriptRunnerMTTest {
    @Test
    public void canRunMultithreaded() throws Exception {
        final ScriptRunner runner = new ScriptRunner().init(new EnvJs(), new Classpath("js/jquery/jquery-1.9.1-min.js"),
                new Classpath("js/test-script.js"));
        for (int i = 0; i < 100; i++) {
            runner.run("doIt", String.class, 1, 2);
        }
        long start = System.currentTimeMillis();
        int threadCount = 10;
        final RunScript[] threads = new RunScript[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new RunScript(runner, i);
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            threads[i].throwIfError();
        }
        System.out.println((threadCount * 1000) + " function executions took: " + (System.currentTimeMillis() - start) + " ms");
        runner.destroy();
    }

    private static class RunScript extends Thread {
        private ScriptRunner runner;
        private int id;
        private Exception e;

        public RunScript(ScriptRunner runner, int id) {
            this.runner = runner;
            this.id = id;
        }

        public void throwIfError() throws Exception {
            if (e != null) {
                throw e;
            }
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    assertEquals("0 -> " + id + ",1 -> 2", runner.run("doIt", String.class, id, 2));
                }
            } catch (Exception e) {
                this.e = e;
            }
        }
    }

}
