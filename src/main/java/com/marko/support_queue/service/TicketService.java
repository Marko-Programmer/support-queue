package com.marko.support_queue.service;

import com.marko.support_queue.dto.CreateTicketRequest;
import com.marko.support_queue.dto.TicketResponse;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    TicketResponse getTicket(UUID id);

    List<TicketResponse> getQueue(int limit);
}