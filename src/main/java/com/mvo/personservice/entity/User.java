package com.mvo.personservice.entity;

import dto.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private UUID id;

    @Column("secret_key")
    private String secretKey;

    @Column("created")
    private LocalDateTime created;

    @Column("updated")
    private LocalDateTime updated;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("verified_at")
    private LocalDateTime verifiedAt;

    @Column("archived_at")
    private LocalDateTime archivedAt;

    @Column("Status")
    private Status status;

    @Column("filled")
    private Boolean filled;

    @Column("address_id")
    private UUID addressId;

}
