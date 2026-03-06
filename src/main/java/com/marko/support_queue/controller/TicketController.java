package com.marko.support_queue.controller;

import com.marko.support_queue.dto.CreateTicketRequest;
import com.marko.support_queue.dto.TicketResponse;
import com.marko.support_queue.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;


    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse ticket = ticketService.createTicket(request);

        return ResponseEntity
                .created(URI.create("/tickets/" + ticket.getId()))
                .body(ticket);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable UUID id) {
        TicketResponse ticket = ticketService.getTicket(id);

        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ticket);
    }


    @GetMapping("/queue")
    public ResponseEntity<List<TicketResponse>> getQueue(@RequestParam(defaultValue = "50") int limit) {
        List<TicketResponse> queue = ticketService.getQueue(limit);
        return ResponseEntity.ok(queue);
    }


}