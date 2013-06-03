package mrowbotham.horn.jasmine;

import mrowbotham.horn.dependencies.EnvJs;
import mrowbotham.horn.dependencies.FileGlob;
import mrowbotham.horn.dependencies.Jasmine;
import mrowbotham.horn.dependencies.Javascript;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class WithJasmineYmlFactory implements JavascriptsFactory {
    @Override
    public List<Javascript> create(Class testClass) {
        try {
            final WithJasmineYml annotation = (WithJasmineYml)testClass.getAnnotation(WithJasmineYml.class);

            final FileReader reader = new FileReader(annotation.file());
            final Map<String, ?> yml = (Map<String, ?>)new Yaml().load(reader);
            final String srcDir = yml.get("src_dir") != null ? (String) yml.get("src_dir") : ".";
            final String specDir = yml.get("spec_dir") != null ? (String) yml.get("spec_dir") : "spec/javascripts";

            final List<Javascript> dependencies = new ArrayList<>();
            dependencies.add(new EnvJs());
            dependencies.add(new Jasmine());
            for (String mainPath : stringListWithDefault(yml, "src_files", Collections.<String>emptyList())) {
                dependencies.add(new FileGlob(srcDir, mainPath));
            }
            for (String helper : stringListWithDefault(yml, "helpers", asList("helpers/**/*.js"))) {
                dependencies.add(new FileGlob(specDir, helper));
            }
            for (String testPath : stringListWithDefault(yml, "spec_files", asList("**/*[sS]pec.js"))) {
                dependencies.add(new FileGlob(specDir, testPath));
            }
            reader.close();
            return dependencies;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> stringListWithDefault(Map<String, ?> yml, String key, List<String> defaults) {
        return yml.get(key) != null ? (List<String>)yml.get(key) : defaults;
    }
}
