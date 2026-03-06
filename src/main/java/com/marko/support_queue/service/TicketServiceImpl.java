package com.marko.support_queue.service;

import com.marko.support_queue.dto.CreateTicketRequest;
import com.marko.support_queue.dto.TicketResponse;
import com.marko.support_queue.model.Ticket;
import com.marko.support_queue.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SlaService slaService;

    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = request.toEntity();

        ticket.setId(UUID.randomUUID());
        ticket.setDueAt(slaService.calculateDueAt(
                ticket.getCreatedAt(),
                ticket.getSeverity(),
                ticket.getCustomerTier()
        ));

        ticketRepository.save(ticket);

        return TicketResponse.from(ticket);
    }

    @Override
    public TicketResponse getTicket(UUID id) {
        return ticketRepository.findById(id)
                .map(TicketResponse::from)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
    }

    @Override
    public List<TicketResponse> getQueue(int limit) {
        return ticketRepository.findAll().stream()
                .sorted(Comparator
                        .comparing(Ticket::getDueAt)
                        .thenComparing(Ticket::getSeverity)
                        .thenComparing(t -> t.getCustomerTier().ordinal())
                        .thenComparing(Ticket::getCreatedAt)
                )
                .limit(limit)
                .map(TicketResponse::from)
                .collect(Collectors.toList());
    }
}