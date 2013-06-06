package mrowbotham.horn.jasmine;

import mrowbotham.horn.dependencies.Javascript;

import java.lang.annotation.Annotation;
import java.util.List;

public interface JavascriptsFactory<T extends Annotation> {
    List<Javascript> create(T annotation);
}
