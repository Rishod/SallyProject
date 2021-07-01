package com.sally.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Setter
    @Getter
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
