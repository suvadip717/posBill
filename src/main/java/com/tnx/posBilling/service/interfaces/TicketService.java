package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.model.Ticket;

public interface TicketService {

    public ResponseEntity<Ticket> createTicket(String email, String phoneNumber, String subject,
            String description,
            String issue, String subIssue, String prefix, MultipartFile photo);

    public ResponseEntity<Ticket> updateTicket(Long id, String email, String phoneNumber, String subject,
            String description,
            String issue, String subIssue, MultipartFile photo, String status);

    public List<Ticket> allToken();

    public ResponseEntity<Ticket> getTicketById(Long id);

    public ResponseEntity<String> deleteTicket(Long id);

    public ResponseEntity<List<Ticket>> getTicketsByUser(Integer userId);

    public ResponseEntity<Ticket> resolvedTicket(Long id);
}
