package com.marko.support_queue.dto;

import com.marko.support_queue.enums.Category;
import com.marko.support_queue.enums.CustomerTier;

import com.marko.support_queue.model.Ticket;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private UUID id;

    private OffsetDateTime createdAt;

    private int severity;

    private CustomerTier customerTier;

    private Category category;

    private String summary;

    private OffsetDateTime dueAt;

    public static TicketResponse from(Ticket ticket) {
        if (ticket == null) return null;
        return TicketResponse.builder()
                .id(ticket.getId())
                .summary(ticket.getSummary())
                .dueAt(ticket.getDueAt())
                .createdAt(ticket.getCreatedAt())
                .severity(ticket.getSeverity())
                .customerTier(ticket.getCustomerTier())
                .category(ticket.getCategory())
                .build();
    }
}