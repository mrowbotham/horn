package mrowbotham.horn.logging;

import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger("Javascript");

    @Override
    public void log(String msg) {
        log.info(msg);
    }
}
