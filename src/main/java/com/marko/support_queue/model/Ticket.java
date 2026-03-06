package com.marko.support_queue.model;

import com.marko.support_queue.enums.Category;
import com.marko.support_queue.enums.CustomerTier;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private UUID id;

    private OffsetDateTime createdAt;

    private int severity;

    private CustomerTier customerTier;

    private Category category;

    private String summary;

    private OffsetDateTime dueAt;
}