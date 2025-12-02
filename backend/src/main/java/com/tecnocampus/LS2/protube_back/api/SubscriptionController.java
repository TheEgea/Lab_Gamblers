package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.application.subscription.SubscriptionService;
import com.tecnocampus.LS2.protube_back.application.user.UserService;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<SubscriptionResponse> subscribe(
            Authentication authentication,
            @RequestBody SubscriptionRequest request) {

        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);

        String username = authentication.getName();
        User user = userService.loadByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User " + username + " not found"));
        UUID userId = user.id().value();

        SubscriptionResponse response = subscriptionService.subscribe(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{channelName}")
    public ResponseEntity<Void> unsubscribe(
            Authentication authentication,
            @PathVariable String channelName) {

        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);

        String username = authentication.getName();
        User user = userService.loadByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User " + username + " not found"));
        UUID userId = user.id().value();

        subscriptionService.unsubscribe(userId, channelName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(Authentication authentication) {

        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);

        String username = authentication.getName();
        User user = userService.loadByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User " + username + " not found"));
        UUID userId = user.id().value();

        List<SubscriptionResponse> subscriptions = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/check/{channelName}")
    public ResponseEntity<Boolean> isSubscribed(
            Authentication authentication,
            @PathVariable String channelName) {

        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);

        String username = authentication.getName();
        User user = userService.loadByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User " + username + " not found"));
        UUID userId = user.id().value();

        boolean isSubscribed = subscriptionService.isSubscribed(userId, channelName);
        return ResponseEntity.ok(isSubscribed);
    }
}
