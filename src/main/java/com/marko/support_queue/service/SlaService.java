package com.marko.support_queue.service;

import com.marko.support_queue.enums.CustomerTier;

import java.time.OffsetDateTime;

public interface SlaService {

    OffsetDateTime calculateDueAt(
            OffsetDateTime createdAt,
            int severity,
            CustomerTier tier
    );
}