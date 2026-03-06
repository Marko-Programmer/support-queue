package com.marko.support_queue.service;

import com.marko.support_queue.enums.CustomerTier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlaServiceImplTest {

    private final SlaService slaService = new SlaServiceImpl();

    @Test
    @DisplayName("Case 1: Wed 10:00 + 4h SLA = Wed 14:00")
    void shouldCalculateSlaWithinSameDay() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-02-11T10:00:00+01:00");

        OffsetDateTime dueAt = slaService.calculateDueAt(createdAt, 1, CustomerTier.ENTERPRISE);

        assertDateTime(dueAt, "2026-02-11T14:00:00+01:00");
    }


    @Test
    @DisplayName("Case 2: Wed 16:30 + 2h SLA = Thu 10:30 (Overnight)")
    void shouldCalculateSlaOvernight() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-02-11T16:30:00+01:00");

        OffsetDateTime dueAt = slaService.calculateDueAt(createdAt, 1, CustomerTier.ENTERPRISE);

        assertDateTime(dueAt, "2026-02-12T12:30:00+01:00");
    }


    @Test
    @DisplayName("Case 3: Sat 11:00 + 8h SLA = Mon 17:00 (Weekend)")
    void shouldCalculateSlaOverWeekend() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-02-14T11:00:00+01:00");

        OffsetDateTime dueAt = slaService.calculateDueAt(createdAt, 2, CustomerTier.ENTERPRISE);

        assertDateTime(dueAt, "2026-02-16T17:00:00+01:00");
    }


    private void assertDateTime(OffsetDateTime actual, String expectedIso) {
        assertEquals(
                OffsetDateTime.parse(expectedIso).toInstant(),
                actual.toInstant(),
                "SLA calculation mismatch"
        );
    }
}