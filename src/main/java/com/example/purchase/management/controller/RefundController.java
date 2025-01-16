package com.example.purchase.management.controller;

import com.example.purchase.management.entity.Refund;
import com.example.purchase.management.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<Refund> createRefund(@RequestBody Refund refund) {
        try {
            return ResponseEntity.ok(refundService.createRefund(refund));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Refund>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }
}
