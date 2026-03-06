package com.marko.support_queue.service;


import com.marko.support_queue.enums.CustomerTier;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Map;

@Service
public class SlaServiceImpl implements SlaService {

    private static final ZoneId ZONE = ZoneId.of("Europe/Warsaw");
    private static final LocalTime START = LocalTime.of(9, 0);
    private static final LocalTime END = LocalTime.of(17, 0);

    private static final Map<CustomerTier, Map<Integer, Integer>> SLA_MAP = Map.of(
            CustomerTier.ENTERPRISE, Map.of(
                    1, 4, 2, 8, 3, 24, 4, 72, 5, 120
            ),
            CustomerTier.PRO, Map.of(
                    1, 8, 2, 16, 3, 48, 4, 96, 5, 168
            ),
            CustomerTier.FREE, Map.of(
                    1, 24, 2, 48, 3, 72, 4, 120, 5, 240
            )
    );

    @Override
    public OffsetDateTime calculateDueAt(OffsetDateTime createdAt, int severity, CustomerTier tier) {
        ZonedDateTime current = createdAt.atZoneSameInstant(ZONE);

        current = normalizeToBusinessTime(current);

        int slaHours = SLA_MAP.get(tier).get(severity);
        long remainingMinutes = slaHours * 60L;

        while (remainingMinutes > 0) {
            ZonedDateTime endOfDay = current.withHour(END.getHour()).withMinute(END.getMinute()).withSecond(0).withNano(0);

            long minutesLeftToday = Duration.between(current, endOfDay).toMinutes();

            if (minutesLeftToday >= remainingMinutes) {
                current = current.plusMinutes(remainingMinutes);
                remainingMinutes = 0;
            } else {
                remainingMinutes -= minutesLeftToday;
                current = nextBusinessDayStart(current);
            }
        }

        return current.toOffsetDateTime();
    }

    private ZonedDateTime normalizeToBusinessTime(ZonedDateTime time) {
        DayOfWeek dow = time.getDayOfWeek();
        LocalTime lt = time.toLocalTime();

        if (dow == DayOfWeek.SATURDAY) {
            time = time.plusDays(2).withHour(START.getHour()).withMinute(START.getMinute()).withSecond(0).withNano(0);
        } else if (dow == DayOfWeek.SUNDAY) {
            time = time.plusDays(1).withHour(START.getHour()).withMinute(START.getMinute()).withSecond(0).withNano(0);
        } else if (lt.isBefore(START)) {
            time = time.withHour(START.getHour()).withMinute(START.getMinute()).withSecond(0).withNano(0);
        } else if (lt.isAfter(END) || lt.equals(END)) {
            time = nextBusinessDayStart(time);
        }

        return time;
    }

    private ZonedDateTime nextBusinessDayStart(ZonedDateTime time) {
        ZonedDateTime next = time.plusDays(1).withHour(START.getHour()).withMinute(START.getMinute()).withSecond(0).withNano(0);
        DayOfWeek dow = next.getDayOfWeek();

        if (dow == DayOfWeek.SATURDAY) {
            next = next.plusDays(2);
        } else if (dow == DayOfWeek.SUNDAY) {
            next = next.plusDays(1);
        }
        return next;
    }
}