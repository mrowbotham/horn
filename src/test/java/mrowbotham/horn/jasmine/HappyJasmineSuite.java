package mrowbotham.horn.jasmine;

import org.junit.runner.RunWith;

@RunWith(JasmineSuite.class)
@WithJavascript(main = {"js/jasmine/src/Player.js", "js/jasmine/src/Song.js"},
                test = {"js/jasmine/spec/SpecHelper.js", "js/jasmine/spec/PlayerSpec.js"})
public class HappyJasmineSuite {

}
