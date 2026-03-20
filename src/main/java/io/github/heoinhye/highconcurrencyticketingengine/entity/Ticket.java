package io.github.heoinhye.highconcurrencyticketingengine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;

    private String seatName;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public void markAsServed(){
        this.status = TicketStatus.RESERVED;
    }
}
