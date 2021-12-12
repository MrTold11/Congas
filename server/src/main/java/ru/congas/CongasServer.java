package ru.congas;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * @author Mr_Told
 */
public class CongasServer {

    public static Logger logger = null;

    public static void main(String[] args) {
        try {
            logger = LogManager.getLogger(CongasServer.class);
            logger.info("Starting Congas server");

            HttpServer server = HttpServer.create(new InetSocketAddress(26642), 0);
            server.createContext("/jar", new GetJarHandler());
            server.createContext("/list", new GetListHandler());
            server.setExecutor(null);
            server.start();

            //Runtime.getRuntime().addShutdownHook(new Thread(stop));

        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init");
                e.printStackTrace();
            } else
                logger.fatal("Failed to start Congas server", e);
        }
    }

}
