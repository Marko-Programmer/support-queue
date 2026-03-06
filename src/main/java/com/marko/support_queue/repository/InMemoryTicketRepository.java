package com.marko.support_queue.repository;

import com.marko.support_queue.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final Map<UUID, Ticket> storage = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        storage.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(storage.values());
    }
}