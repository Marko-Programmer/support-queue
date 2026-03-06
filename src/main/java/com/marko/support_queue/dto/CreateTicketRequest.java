package com.marko.support_queue.dto;

import com.marko.support_queue.enums.Category;
import com.marko.support_queue.enums.CustomerTier;
import com.marko.support_queue.model.Ticket;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketRequest {

    @NotNull
    private OffsetDateTime createdAt;

    @Min(1)
    @Max(5)
    private int severity;

    @NotNull
    private CustomerTier customerTier;

    @NotNull
    private Category category;

    @NotBlank
    @Size(max = 200)
    private String summary;

    public Ticket toEntity() {
        return Ticket.builder()
                .createdAt(this.createdAt)
                .severity(this.severity)
                .customerTier(this.customerTier)
                .category(this.category)
                .summary(this.summary)
                .build();
    }
}