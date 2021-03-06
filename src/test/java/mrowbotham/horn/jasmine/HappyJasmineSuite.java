package mrowbotham.horn.jasmine;

import org.junit.runner.RunWith;

@RunWith(JasmineSuite.class)
@WithJavascript(srcDir = "src/test/resources", srcFiles = {"**/Player.js", "**/Song.js"},
        specDir = "src/test/resources", helpers = {"**/SpecHelper.js"}, specFiles = {"**/PlayerSpec.js"})
public class HappyJasmineSuite {

}
