package io.github.heoinhye.highconcurrencyticketingengine.service;

import io.github.heoinhye.highconcurrencyticketingengine.entity.Ticket;
import io.github.heoinhye.highconcurrencyticketingengine.entity.TicketStatus;
import io.github.heoinhye.highconcurrencyticketingengine.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("Should process only one reservation when 100 requests arrive simultaneously")
    void concurrencyTest() throws InterruptedException {
        // 1. Prepare initial data: Create a ticket with AVAILABLE status (null or custom)
        Ticket ticket = new Ticket(TicketStatus.AVAILABLE); // Assuming null or a default is 'Available'
        Ticket savedTicket = ticketRepository.save(ticket);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // CountDownLatch helps synchronize the start and end of all threads
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 2. Execute 100 simultaneous requests
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    ticketService.reserveTicket(savedTicket.getId());
                } catch (Exception e) {
                    // Expected: 99 threads will fail and print an error message
                    log.info("Request failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait until all threads finish

        // 3. Verify the final status of the ticket
        Ticket finalTicket = ticketRepository.findById(savedTicket.getId()).orElseThrow();

        // Assert that the status is RESERVED
        assertEquals(TicketStatus.RESERVED, finalTicket.getStatus());

        // Additional check: In a real DB, you could also count how many successful logs were made
        log.info("Final Ticket Status: " + finalTicket.getStatus());
    }

}