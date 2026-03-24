package io.github.heoinhye.highconcurrencyticketingengine.service;

import io.github.heoinhye.highconcurrencyticketingengine.entity.Ticket;
import io.github.heoinhye.highconcurrencyticketingengine.entity.TicketStatus;
import io.github.heoinhye.highconcurrencyticketingengine.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RedissonClient redissonClient;

    /**
     * Reserves a ticket for a specific user.
     * @param ticketId The unique identifier of the ticket to be reserved
     * @throws IllegalArgumentException if the ticket does not exist
     * @throws IllegalStateException if the ticket is already reserved or sold
     * */
    @Transactional
    public void reserveTicket(Long ticketId){

        RLock lock = redissonClient.getLock("ticket_lock:" + ticketId);

        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if(!available){
                throw new RuntimeException("Lock acquisition failed - Try again later");
            }

            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> {
                        log.error("Target ticket not found. ID: {}", ticketId);
                        return new IllegalArgumentException("Ticket not found");
                    });

            if(ticket.getStatus() != TicketStatus.AVAILABLE){
                log.warn("Reservation failed - Ticket {} is already {}", ticketId, ticket.getStatus());
                throw new IllegalStateException("Not available");
            }

            ticket.markAsServed();
            ticketRepository.save(ticket);
            log.info("Successfully reserved ticket ID: {}", ticketId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            // CRITICAL: Always release the lock if it's held by the current thread
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
