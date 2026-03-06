package com.marko.support_queue.repository;

import com.marko.support_queue.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> findById(UUID id);

    List<Ticket> findAll();
}