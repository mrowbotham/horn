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

public class WithJasmineYmlFactory implements JavascriptsFactory<WithJasmineYml> {
    @Override
    public List<Javascript> create(WithJasmineYml annotation) {
        try {
            final FileReader reader = new FileReader(annotation.file());
            final Map<String, ?> yml = (Map<String, ?>)new Yaml().load(reader);
            final String srcDir = stringWithDefault(yml, "src_dir", ".");
            final String specDir = stringWithDefault(yml, "spec_dir", "spec/javascripts");

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

    private List<String> stringListWithDefault(Map<String, ?> yml, String key, List<String> def) {
        final Object val = yml.get(key);
        return val == null ? def : (val instanceof List ? (List<String>)val : asList(val.toString()));
    }

    private String stringWithDefault(Map<String, ?> yml, String key, String def) {
        final Object val = yml.get(key);
        return val == null ? def : (val instanceof List ? ((List<String>)val).get(0) : val.toString());
    }
}
