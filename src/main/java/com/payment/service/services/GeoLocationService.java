package com.payment.service.services;

import com.payment.service.clients.IpinfoClient;
import com.payment.service.models.GeoLocation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GeoLocationService {

    private final IpinfoClient ipinfoClient;

    @Autowired
    public GeoLocationService(IpinfoClient ipinfoClient) {
        this.ipinfoClient = ipinfoClient;
    }

    public GeoLocation getGeoLocation(String ip) {
        String jsonResponse = ipinfoClient.getGeoLocation(ip);

        JSONObject jsonObject = new JSONObject(jsonResponse);

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setPublicId(UUID.randomUUID());
        geoLocation.setCity(jsonObject.optString("city"));
        geoLocation.setCountry(jsonObject.optString("country"));
        geoLocation.setRegion(jsonObject.optString("region"));
        geoLocation.setPostalCode(jsonObject.optString("postal"));

        return geoLocation;
    }
}
