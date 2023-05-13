package fr.limayrac.avSpringProject.controller;

import fr.limayrac.avSpringProject.controller.dto.EventDTO;
import fr.limayrac.avSpringProject.exception.ResourceNotFoundException;
import fr.limayrac.avSpringProject.model.Event;
import fr.limayrac.avSpringProject.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("")
    public String getAllEvents(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "flows/events/events";
    }

    @GetMapping("/{id}")
    public String getEventById(@PathVariable(value = "id") Long eventId, Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event id not found ::" + eventId));
        model.addAttribute("event", event);
        return "events/show";
    }

    @GetMapping("/new")
    public String showEventForm(Event event) {
        return "flows/events/create-event";
    }

    @PostMapping("/new")
    public String createEvent(@Valid EventDTO event, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getDate(), formatter);
        Event newEvent = new Event();
        newEvent.setDate(date);
        newEvent.setName(event.getName());
        eventRepository.save(newEvent);
        model.addAttribute("events", eventRepository.findAll());
        return "flows/events/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditEventForm(@PathVariable(value = "id") Long eventId, Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event id not found ::" + eventId));
        model.addAttribute("event", event);
        return "events/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable(value = "id") Long eventId, @Valid Event event, Model model) {
        Event updatedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event id not found ::" + eventId));
        updatedEvent.setName(event.getName());
        updatedEvent.setDate(event.getDate());
        eventRepository.save(updatedEvent);
        model.addAttribute("events", eventRepository.findAll());
        return "redirect:/events";
    }

    @GetMapping("/{id}/delete")
    public String deleteEvent(@PathVariable(value = "id") Long eventId, Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event id not found ::" + eventId));
        eventRepository.delete(event);
        model.addAttribute("events", eventRepository.findAll());
        return "redirect:/events";
    }

}
