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

    @Column("comment")
    private String comment;

    @Column("changed_values")
    private String changedValues;
}
