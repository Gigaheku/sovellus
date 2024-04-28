package com.harjoitus.sovellus.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SomeService {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void adminOnlyMethod() {
        // Admin-only functionality
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void userMethod() {
        // User-specific functionality
    }

    // No annotation needed for methods accessible by visitors
    public void visitorAccessibleMethod() {
        // Visitor-accessible functionality
    }
}
