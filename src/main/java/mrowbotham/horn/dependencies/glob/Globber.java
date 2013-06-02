package mrowbotham.horn.dependencies.glob;

import java.util.regex.Pattern;

public class Globber {

    public static Pattern globToRegex(String glob) {
        final StringBuilder regexBuilder = new StringBuilder();
        char[] globChars = glob.toCharArray();
        int inCurlys = 0;
        for (int i = 0; i < globChars.length; i++) {
            char c = globChars[i];
            if (c == '\\') {
                regexBuilder.append(c);
                if (i < globChars.length - 1) {
                    regexBuilder.append(globChars[i+1]);
                }
                i++;
            } else if(c == '{') {
                regexBuilder.append("(");
                inCurlys++;
            } else if(c == '}') {
                regexBuilder.append(")");
                inCurlys--;
            } else if(c == ',' && inCurlys > 0) {
                regexBuilder.append("|");
            } else if (c == '*' && i < globChars.length - 1 && globChars[i+1] == '*') {
                regexBuilder.append(".*");
                i++;
                if (i < globChars.length - 1 && globChars[i+1] == '/') {
                    i++;
                }
            } else if (c == '*' && (i == 0 || (globChars[i-1] != '}' && globChars[i-1] != ']'))) {
                regexBuilder.append("[^/]*");
            } else if (c == '?') {
                regexBuilder.append("\\w");
            } else if (c =='.') {
                regexBuilder.append("\\.");
            } else {
                regexBuilder.append(c);
            }
        }
        return Pattern.compile(regexBuilder.toString());
    }

}
