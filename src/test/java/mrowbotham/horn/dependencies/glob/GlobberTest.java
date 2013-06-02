package mrowbotham.horn.dependencies.glob;

import org.junit.Test;

import static mrowbotham.horn.dependencies.glob.Globber.globToRegex;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GlobberTest {
    @Test
    public void rubyQuestionMark() throws Exception {
        assertTrue(globToRegex("config.?").matcher("config.h").matches());
        assertFalse(globToRegex("config.?").matcher("config.java").matches());
    }

    @Test
    public void rubySet() throws Exception {
        assertTrue(globToRegex("*.[a-z][a-z]").matcher("main.rb").matches());
        assertFalse(globToRegex("*.[a-z][a-z]").matcher("config.h").matches());
        assertTrue(globToRegex("*.[p-t][a-c]").matcher("main.rb").matches());
        assertFalse(globToRegex("*.[A-Z]").matcher("config.h").matches());
    }

    @Test
    public void rubyNegativeSet() throws Exception {
        assertTrue(globToRegex("*.[^r]*").matcher("config.h").matches());
        assertFalse(globToRegex("*.[^r]*").matcher("main.rb").matches());
    }

    @Test
    public void rubyAlternate() throws Exception {
        assertTrue(globToRegex("*.{rb,h}").matcher("config.h").matches());
        assertTrue(globToRegex("*.{rb,h}").matcher("main.rb").matches());
        assertFalse(globToRegex("*.{rb,h}").matcher("Main.java").matches());
    }

    @Test
    public void rubyStar() throws Exception {
        assertTrue(globToRegex("*").matcher("config.h").matches());
        assertTrue(globToRegex("*").matcher("main.rb").matches());
        assertFalse(globToRegex("*").matcher("lib/native/browse.exe").matches());
    }

    @Test
    public void rubyDoubleStar() throws Exception {
        assertTrue(globToRegex("**/*.rb").matcher("main.rb").matches());
        assertTrue(globToRegex("**/*.rb").matcher("lib/song.rb").matches());
        assertTrue(globToRegex("**/*.rb").matcher("lib/song/karaoke.rb").matches());
        assertTrue(globToRegex("**/lib").matcher("lib").matches());
        assertFalse(globToRegex("**/*.rb").matcher("config.h").matches());
        assertFalse(globToRegex("**/*.rb").matcher("lib/config.h").matches());
        assertTrue(globToRegex("**/lib/**/*.rb").matcher("lib/main.rb").matches());
        assertTrue(globToRegex("**/lib/**/*.rb").matcher("lib/more/main.rb").matches());
    }

    @Test
    public void rubySpecs() throws Exception {
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("A-Spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("B-spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("c_Spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("D_spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/A-Spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/B-spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/c_Spec.js").matches());
        assertTrue(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/D_spec.js").matches());
        assertFalse(globToRegex("**/*[_-][Ss]pec.js").matcher("ASpec.js").matches());
        assertFalse(globToRegex("**/*[_-][Ss]pec.js").matcher("B-opec.js").matches());
        assertFalse(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/cSpec.js").matches());
        assertFalse(globToRegex("**/*[_-][Ss]pec.js").matcher("one/two/D_opec.js").matches());
    }
}
