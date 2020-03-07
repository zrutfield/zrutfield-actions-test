package com.zrutfield.services.beer.service;

import com.zrutfield.beer.v1.Beer;
import com.zrutfield.beer.v1.BeerServiceGrpc;
import com.zrutfield.beer.v1.CreateBeerRequest;
import com.zrutfield.beer.v1.CreateBeerResponse;
import com.zrutfield.beer.v1.DeleteBeerRequest;
import com.zrutfield.beer.v1.DeleteBeerResponse;
import com.zrutfield.beer.v1.GetBeerRequest;
import com.zrutfield.beer.v1.GetBeerResponse;
import com.zrutfield.beer.v1.UpdateBeerRequest;
import com.zrutfield.beer.v1.UpdateBeerResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BeerService extends BeerServiceGrpc.BeerServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeerService.class);

    private Map<String, Beer> beerFridge = new HashMap<>();

    private String foo;

    @Inject
    public BeerService(@Named("FooString") String foo) {
        this.foo = foo;
    }

    @Override
    public void getBeer(GetBeerRequest request, StreamObserver<GetBeerResponse> responseObserver) {
        LOGGER.info(request.toString());
        LOGGER.info(foo);
        String beerId = request.getId();

        if (beerFridge.containsKey(beerId)) {
            GetBeerResponse response = GetBeerResponse.newBuilder()
                    .setBeer(beerFridge.get(beerId))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(String.format("BeerNotFoundException for id %s",  beerId))
                    .asException());
        }
    }

    @Override
    public void createBeer(CreateBeerRequest request, StreamObserver<CreateBeerResponse> responseObserver) {
        LOGGER.info(request.toString());
        LOGGER.info(foo);

        try {
            String uuid = UUID.randomUUID().toString();
            Beer beer = request.getBeer();

            beerFridge.put(uuid, beer);

            Beer storedBeer = beer.toBuilder().setId(uuid).build();
            CreateBeerResponse response = CreateBeerResponse.newBuilder().setBeer(storedBeer).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withCause(e).withDescription(e.toString()).asException());
        }
    }

    @Override
    public void updateBeer(UpdateBeerRequest request, StreamObserver<UpdateBeerResponse> responseObserver) {
        LOGGER.info(request.toString());

        String beerId = request.getBeer().getId();
        Beer beer = request.getBeer();

        if (beerFridge.containsKey(beerId)) {
            beerFridge.put(beerId, beer);
            UpdateBeerResponse response = UpdateBeerResponse.newBuilder()
                    .setBeer(beer)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(String.format("BeerNotFoundException for id %s",  beerId))
                    .asException());
        }

    }

    @Override
    public void deleteBeer(DeleteBeerRequest request, StreamObserver<DeleteBeerResponse> responseObserver) {
        LOGGER.info(request.toString());

        String beerId = request.getId();

        if (beerFridge.containsKey(beerId)) {
            beerFridge.remove(beerId);
            DeleteBeerResponse response = DeleteBeerResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(String.format("BeerNotFoundException for id %s",  beerId))
                    .asException());
        }
    }


}
