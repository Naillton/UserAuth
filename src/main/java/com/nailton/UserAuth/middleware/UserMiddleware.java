package com.nailton.UserAuth.middleware;

import org.springframework.http.ResponseEntity;

public interface UserMiddleware {
    ResponseEntity<String> validCamps(String email, String name, String password);

}
