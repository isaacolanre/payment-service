package com.payment.service.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class GeoLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String country;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String postalCode;

}
