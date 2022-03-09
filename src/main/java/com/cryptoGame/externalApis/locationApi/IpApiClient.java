package com.cryptoGame.externalApis.locationApi;

import com.cryptoGame.domain.dtos.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class IpApiClient {

    private final RestTemplate restTemplate;

    public LocationDto getUserLocation(){
        URI url = UriComponentsBuilder.fromHttpUrl("https://ipapi.co/json/")
                .build().encode().toUri();
        return restTemplate.getForObject(url, LocationDto.class);
    }
}
