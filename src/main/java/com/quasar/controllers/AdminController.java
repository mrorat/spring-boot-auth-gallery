package com.quasar.controllers;

import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    public AdminController() {
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(
        value = {"/administration"},
        method = {RequestMethod.GET}
    )
    public String getUserProfilePage(@RequestParam Optional<String> error) {
        return "admin/administration";
    }
}
