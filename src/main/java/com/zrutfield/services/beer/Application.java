package com.zrutfield.services.beer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new GrpcModule());

        Server server = injector.getInstance(Server.class);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownHook(server)));

        server.start();
        LOGGER.info("Server running on port 8980");
        server.awaitTermination();
    }

    private static void shutdownHook(Server server) {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        LOGGER.info("*** shutting down gRPC server since JVM is shutting down");
        server.shutdown();
        LOGGER.info("*** server shut down");
    }
}
