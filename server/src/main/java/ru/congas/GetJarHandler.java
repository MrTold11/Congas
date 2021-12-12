package ru.congas;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.network.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mr_Told
 */
public class GetJarHandler implements HttpHandler {

    final Logger logger = LogManager.getLogger("HttpHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String jarName = NetworkUtils.queryToMap(exchange.getRequestURI().getQuery()).get("name");
        if (jarName == null || jarName.length() == 0) {
            logger.warn("Got unknown jar request: " + exchange.getRequestURI().getQuery());
            exchange.sendResponseHeaders(404, 0);
            return;
        }
        logger.info("Got jar request");
        InputStream in = exchange.getRequestBody();
    }

}
