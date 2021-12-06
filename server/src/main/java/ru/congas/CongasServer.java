package ru.congas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Mr_Told
 */
public class CongasServer {

    public static Logger logger = null;

    public static void main(String[] args) {
        try {
            logger = LogManager.getLogger(CongasServer.class);
            Hello h = new Hello();
            logger.info("Starting Congas server" + h.hello);

            //Runtime.getRuntime().addShutdownHook(new Thread(world::stop));

        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init");
                e.printStackTrace();
            } else
                logger.fatal("Failed to start Congas server", e);
        }
    }

}
