package mrowbotham.horn.jasmine;

import mrowbotham.horn.dependencies.Javascript;

import java.util.List;

public interface JavascriptsFactory {
    List<Javascript> create(Class testClass);
}
