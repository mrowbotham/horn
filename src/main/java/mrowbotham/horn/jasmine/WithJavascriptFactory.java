package mrowbotham.horn.jasmine;

import mrowbotham.horn.dependencies.EnvJs;
import mrowbotham.horn.dependencies.FileGlob;
import mrowbotham.horn.dependencies.Jasmine;
import mrowbotham.horn.dependencies.Javascript;

import java.util.ArrayList;
import java.util.List;

public class WithJavascriptFactory implements JavascriptsFactory<WithJavascript> {
    @Override
    public List<Javascript> create(WithJavascript annotation) {
        final List<Javascript> dependencies = new ArrayList<>();
        dependencies.add(new EnvJs());
        dependencies.add(new Jasmine());
        for (String mainPath : annotation.srcFiles()) {
            dependencies.add(new FileGlob(annotation.srcDir(), mainPath));
        }
        for (String helper : annotation.helpers()) {
            dependencies.add(new FileGlob(annotation.srcDir(), helper));
        }
        for (String testPath : annotation.specFiles()) {
            dependencies.add(new FileGlob(annotation.specDir(), testPath));
        }
        return dependencies;
    }
}
