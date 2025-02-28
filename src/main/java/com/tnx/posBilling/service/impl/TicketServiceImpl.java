package com.tnx.posBilling.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Ticket;
import com.tnx.posBilling.model.User;
import com.tnx.posBilling.repository.TicketRepository;
import com.tnx.posBilling.repository.UserRepository;
import com.tnx.posBilling.service.AwsS3Service;
import com.tnx.posBilling.service.SendEmailService;
import com.tnx.posBilling.service.interfaces.TicketService;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AwsS3Service aS3Service;

    @Autowired
    private SendEmailService mailService;

    @Autowired
    private UserRepository userRepository;

    public String generateRandomString() {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    @Override
    public ResponseEntity<Ticket> createTicket(String email, String phoneNumber, String subject,
            String description,
            String issue, String subIssue, String prefix, MultipartFile photo) {
        Ticket newTicket = new Ticket();
        try {
            // Map the user JSON to a User object
            User myUser = userRepository.findById(2)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Upload the image (if provided) to S3
            String imageUrl = aS3Service.saveImageToS3(photo);

            // Set ticket fields
            newTicket.setReferenceId(prefix + generateRandomString());
            newTicket.setEmail(email);
            newTicket.setPhoneNumber(phoneNumber);
            newTicket.setSubject(subject);
            newTicket.setDescription(description);
            newTicket.setIssue(issue);
            newTicket.setSubIssue(subIssue);
            newTicket.setAttachMent(imageUrl);
            newTicket.setStatus("Pending");
            newTicket.setCreatedAt(LocalDate.now());
            newTicket.setExpectedDate(LocalDate.now().plusDays(8));
            mailService.sendVerificationEmail(email, subject, newTicket.getExpectedDate(), newTicket.getReferenceId());
            newTicket.setCreatedByUser(myUser);

            // Save the ticket to the repository
            return new ResponseEntity<>(ticketRepository.save(newTicket), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid user data provided");
        }
    }

    @Override
    public ResponseEntity<Ticket> updateTicket(Long id, String email, String phoneNumber, String subject,
            String description,
            String issue, String subIssue, MultipartFile photo, String status) {
        String imageUrl = aS3Service.saveImageToS3(photo);
        Ticket exTicket = ticketRepository.findById(id).map(ticket -> {
            ticket.setEmail(email);
            ticket.setPhoneNumber(phoneNumber);
            ticket.setSubject(subject);
            ticket.setDescription(description);
            ticket.setIssue(issue);
            ticket.setSubIssue(subIssue);
            ticket.setAttachMent(imageUrl);
            ticket.setStatus(status);
            ticket.setUpdatedAt(LocalDate.now());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ResponseEntity.ok(exTicket);
    }

    @Override
    public List<Ticket> allToken() {
        return ticketRepository.findAll();
    }

    @Override
    public ResponseEntity<Ticket> getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ResponseEntity.ok(ticket);
    }

    @Override
    public ResponseEntity<String> deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
        return ResponseEntity.ok("Ticket deleted");
    }

    @Override
    public ResponseEntity<List<Ticket>> getTicketsByUser(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(ticketRepository.findByCreatedByUserId(userId));
    }

    @Override
    public ResponseEntity<Ticket> resolvedTicket(Long id) {
        Ticket exTicket = ticketRepository.findById(id).map(ticket -> {
            ticket.setStatus("Solved");
            ticket.setResolvedAt(LocalDate.now());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ResponseEntity.ok(exTicket);
    }
}
