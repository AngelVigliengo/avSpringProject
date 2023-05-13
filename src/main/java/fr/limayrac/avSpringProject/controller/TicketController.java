package fr.limayrac.avSpringProject.controller;

import fr.limayrac.avSpringProject.controller.dto.TicketDTO;
import fr.limayrac.avSpringProject.exception.ResourceNotFoundException;
import fr.limayrac.avSpringProject.model.Event;
import fr.limayrac.avSpringProject.model.Ticket;
import fr.limayrac.avSpringProject.repository.EventRepository;
import fr.limayrac.avSpringProject.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("")
    public String getAllTickets(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "flows/tickets/tickets";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable(value = "id") Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(ticket.get());
    }

    @GetMapping("/new")
    public String showTicketForm(Ticket ticket, Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "flows/tickets/create-ticket";
    }

    @PostMapping("/new")
    public String createTicket(@Valid TicketDTO ticket, Model model) {
        Ticket newTicket = new Ticket();
        Event event = eventRepository.findById(ticket.getEvent())
                        .orElseThrow(() -> new ResourceNotFoundException("Event id not found ::" + ticket.getEvent()));
        newTicket.setType(ticket.getType());
        newTicket.setPrice(ticket.getPrice());
        newTicket.setEvent(event);
        ticketRepository.save(newTicket);
        model.addAttribute("tickets", ticketRepository.findAll());
        return "flows/tickets/tickets";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable(value = "id") Long ticketId, @Valid @RequestBody Ticket ticketDetails) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ticket ticket = optionalTicket.get();
        ticket.setType(ticketDetails.getType());
        ticket.setPrice(ticketDetails.getPrice());
        ticket.setEvent(ticketDetails.getEvent());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable(value = "id") Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ticketRepository.delete(ticket.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/{eventId}")
    public List<Ticket> getAllTicketsForEvent(@PathVariable(value = "eventId") Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return null;
        }
        return ticketRepository.findByEvent(event);
    }
}
