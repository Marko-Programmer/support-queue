package com.marko.support_queue.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.support_queue.dto.CreateTicketRequest;
import com.marko.support_queue.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketLoader implements CommandLineRunner {

    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = new ClassPathResource("tickets.sample.json").getInputStream();
        List<CreateTicketRequest> tickets = objectMapper.readValue(
                inputStream,
                new TypeReference<List<CreateTicketRequest>>() {}
        );

        tickets.forEach(ticketService::createTicket);

        System.out.println("Loaded " + tickets.size() + " sample tickets");
    }
}