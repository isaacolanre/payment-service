package com.payment.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ipinfoClient", url = "https://ipinfo.io")
public interface IpinfoClient {

    @GetMapping("/{ip}/json")
    String getGeoLocation(@PathVariable("ip") String ip);
}
