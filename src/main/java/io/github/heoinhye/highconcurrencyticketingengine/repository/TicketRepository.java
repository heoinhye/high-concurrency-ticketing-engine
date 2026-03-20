package io.github.heoinhye.highconcurrencyticketingengine.repository;

import io.github.heoinhye.highconcurrencyticketingengine.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
