package com.marko.support_queue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.support_queue.dto.CreateTicketRequest;
import com.marko.support_queue.dto.TicketResponse;
import com.marko.support_queue.enums.Category;
import com.marko.support_queue.enums.CustomerTier;
import com.marko.support_queue.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private final TicketService ticketService = Mockito.mock(TicketService.class);
    private final TicketController ticketController = new TicketController(ticketService);

    @Test
    void shouldReturnQueueWithLimit() {
        List<TicketResponse> mockList = List.of(new TicketResponse());
        when(ticketService.getQueue(10)).thenReturn(mockList);

        ResponseEntity<List<TicketResponse>> response = ticketController.getQueue(10);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }


    @Test
    void shouldCreateAndThenGetTicketById() throws Exception {
        CreateTicketRequest request = CreateTicketRequest.builder()
                .summary("Flow test summary")
                .category(Category.PAYMENTS)
                .severity(1)
                .customerTier(CustomerTier.ENTERPRISE)
                .createdAt(OffsetDateTime.now())
                .build();

        MvcResult postResult = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseContent = postResult.getResponse().getContentAsString();
        TicketResponse createdTicket = objectMapper.readValue(responseContent, TicketResponse.class);
        UUID generatedId = createdTicket.getId();

        mockMvc.perform(get("/tickets/" + generatedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.summary").value("Flow test summary"))
                .andExpect(jsonPath("$.customerTier").value("ENTERPRISE"));
    }
}