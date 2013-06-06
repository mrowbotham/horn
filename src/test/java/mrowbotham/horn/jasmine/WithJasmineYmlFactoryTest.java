package mrowbotham.horn.jasmine;

import mrowbotham.horn.dependencies.EnvJs;
import mrowbotham.horn.dependencies.FileGlob;
import mrowbotham.horn.dependencies.Jasmine;
import mrowbotham.horn.dependencies.Javascript;
import org.junit.Test;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class WithJasmineYmlFactoryTest {
    @WithJasmineYml(file = "src/test/resources/jasmine/jasmine-with-values.yml")
    public static class WithJasmineYmlWithValues {}

    @WithJasmineYml(file = "src/test/resources/jasmine/jasmine-with-wrong-types.yml")
    public static class WithJasmineYmlWithWrongTypes {}

    @WithJasmineYml(file = "src/test/resources/jasmine/empty-jasmine.yml")
    public static class WithEmptyJasmine {}

    private final WithJasmineYmlFactory factory = new WithJasmineYmlFactory();

    @Test
    public void jasmineYml() throws Exception {
        final List<Javascript> actual = factory.create(WithJasmineYmlWithValues.class.getAnnotation(WithJasmineYml.class));

        assertNotNull(actual);
        assertEquals(8, actual.size());
        assertEquals(actual.get(0), new EnvJs());
        assertEquals(actual.get(1), new Jasmine());
        assertEquals(actual.get(2), new FileGlob("javascripts", "vendor/**/*.{js,coffee}"));
        assertEquals(actual.get(3), new FileGlob("javascripts", "lib/**/*.{js,coffee}"));
        assertEquals(actual.get(4), new FileGlob("javascripts", "app/**/*.{js,coffee}"));
        assertEquals(actual.get(5), new FileGlob("spec/javascripts", "helpers/**/*.{js,coffee}"));
        assertEquals(actual.get(6), new FileGlob("spec/javascripts", "**/*[Ss]pec.coffee"));
        assertEquals(actual.get(7), new FileGlob("spec/javascripts", "**/*[Ss]pec.js"));
    }

    @Test
    public void jasmineYmlWithWrongTypes() throws Exception {
        final List<Javascript> actual = factory.create(WithJasmineYmlWithWrongTypes.class.getAnnotation(WithJasmineYml.class));

        assertNotNull(actual);
        assertEquals(5, actual.size());
        assertEquals(actual.get(0), new EnvJs());
        assertEquals(actual.get(1), new Jasmine());
        assertEquals(actual.get(2), new FileGlob("javascripts", "vendor/**/*.{js,coffee}"));
        assertEquals(actual.get(3), new FileGlob("spec/javascripts", "helpers/**/*.{js,coffee}"));
        assertEquals(actual.get(4), new FileGlob("spec/javascripts", "**/*[Ss]pec.coffee"));
    }

    @Test
    public void emptyJasmineYml() throws Exception {
        final List<Javascript> actual = factory.create(WithEmptyJasmine.class.getAnnotation(WithJasmineYml.class));

        assertNotNull(actual);
        assertEquals(4, actual.size());
        assertEquals(actual.get(0), new EnvJs());
        assertEquals(actual.get(1), new Jasmine());
        assertEquals(actual.get(2), new FileGlob("spec/javascripts", "helpers/**/*.js"));
        assertEquals(actual.get(3), new FileGlob("spec/javascripts", "**/*[sS]pec.js"));
    }
}
