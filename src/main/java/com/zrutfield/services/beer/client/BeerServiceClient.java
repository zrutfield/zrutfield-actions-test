package com.zrutfield.services.beer.client;

import com.zrutfield.beer.v1.Beer;
import com.zrutfield.beer.v1.BeerServiceGrpc;
import com.zrutfield.beer.v1.CreateBeerRequest;
import com.zrutfield.beer.v1.CreateBeerResponse;
import com.zrutfield.beer.v1.GetBeerRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BeerServiceClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8981)
                .usePlaintext()
                .build();

        BeerServiceGrpc.BeerServiceBlockingStub blockingStub = BeerServiceGrpc.newBlockingStub(channel);

        for(int i = 0; i < 400; ++i) {

            CreateBeerRequest createBeerRequest = CreateBeerRequest.newBuilder()
                    .setBeer(Beer.newBuilder()
                            .setBrand("Miller")
                            .setName("High Life")
                            .setType("Lite")
                            .build())
                    .build();

            CreateBeerResponse response = blockingStub.createBeer(createBeerRequest);
            String beerId = response.getBeer().getId();

            System.out.println(beerId);

            GetBeerRequest getBeerRequest = GetBeerRequest.newBuilder()
                    .setId(beerId)
                    .build();

            Beer beer = blockingStub.getBeer(getBeerRequest).getBeer();

            System.out.println(beer.getName());
        }

        channel.shutdown();
    }
}
