package com.mvo.personservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("addresses")
public class Address {
    @Id
    private UUID id;

    @Column("created")
    private LocalDateTime created;

    @Column("updated")
    private LocalDateTime updated;

    @Column("country_id")
    private Integer countryId;

    @Column("address")
    private String address;

    @Column("zip_code")
    private String zipCode;

    @Column("archived")
    private LocalDateTime archived;

    @Column("city")
    private String city;

    @Column("state")
    private String state;
}
