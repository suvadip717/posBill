package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.model.Ticket;
import com.tnx.posBilling.service.TicketService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Ticket> creatTicket(
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String subject,
            @RequestParam String description,
            @RequestParam String issue,
            @RequestParam String subIssue,
            @RequestParam MultipartFile photo
    // @RequestParam Integer userId
    ) {
        return ticketService.createTicket(email, phoneNumber, subject, description, issue, subIssue, photo);
    }

    @GetMapping
    public List<Ticket> allToken() {
        return ticketService.allToken();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String subject,
            @RequestParam String description,
            @RequestParam String issue,
            @RequestParam String subIssue,
            @RequestParam MultipartFile photo,
            @RequestParam String status) {
        return ticketService.updateTicket(id, email, phoneNumber, subject, description, issue, subIssue, photo,
                status);
        // return ticket;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Integer userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @PostMapping("/resolved/{id}")
    public ResponseEntity<Ticket> resolveTicket(@PathVariable Long id) {
        return ticketService.resolvedTicket(id);
    }

}
