package com.mvo.personservice.entity;

import dto.status.Status;
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
@Table("individuals")
public class Individual {
    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("created")
    private LocalDateTime created;

    @Column("updated")
    private LocalDateTime updated;

    @Column("passport_number")
    private String passportNumber;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("verified_at")
    private LocalDateTime verifiedAt;

    @Column("archived_at")
    private LocalDateTime archivedAt;

    @Column("status")
    private Status status;
}
