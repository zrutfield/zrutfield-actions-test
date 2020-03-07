package com.zrutfield.services.beer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zrutfield.services.beer.service.BeerService;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.inject.Named;

public class GrpcModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BindableService.class).to(BeerService.class);
    }

    @Provides
    Server buildServer(BindableService bindableService) {
        return ServerBuilder.forPort(8980)
                .addService(bindableService)
                .build();
    }

    @Provides
    @Named("FooString")
    String buildFooString() {
        return "Foo";
    }
}
