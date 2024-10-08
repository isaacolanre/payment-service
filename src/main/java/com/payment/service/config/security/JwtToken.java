package com.payment.service.config.security;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Generated;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sub",
        "iss",
        "roles",
        "authorities",
        "iat",
        "exp",
        "namespace",
        "X-CHANNEL",
        "userAccounts",
        "AGENT",
        "referred-by",
        "referred-by-partner",
        "min-kyc-level"
})
@Generated("jsonschema2pojo")
@Data
public class JwtToken {

    @JsonProperty("sub")
    private String sub;
    @JsonProperty("iss")
    private String iss;
    @JsonProperty("authorities")
    private List<String> authorities = new ArrayList<>();
    @JsonProperty("roles")
    private List<String> roles = new ArrayList<>();
    @JsonProperty("iat")
    private Integer iat;
    @JsonProperty("exp")
    private Integer exp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("userAccounts")
    private List<String> userAccounts;

    @JsonProperty("min-kyc-level")
    private HashMap<String, String> minKycLevel;

}
