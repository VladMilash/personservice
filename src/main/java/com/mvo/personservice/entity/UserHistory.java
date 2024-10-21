package com.mvo.personservice.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import dto.status.Status;
import io.r2dbc.postgresql.codec.Json;
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
@Table("user_history")
public class UserHistory {
    @Id
    private UUID id;

    @Column("created")
    private LocalDateTime created;

    @Column("user_id")
    private UUID userId;

    @Column("user_type")
    private String userType;

    @Column("reason")
    private String reason;

    @Column("changed_values")
    private Json changedValues;
}
