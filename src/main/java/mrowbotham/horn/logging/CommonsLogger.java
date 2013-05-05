package mrowbotham.horn.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonsLogger implements Logger {
    private static final Log log = LogFactory.getLog("Javascript");

    @Override
    public void log(String msg) {
        log.info(msg);
    }
}
