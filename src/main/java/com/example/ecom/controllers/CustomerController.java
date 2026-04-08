package com.example.ecom.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')") //Both allowed
public class CustomerController {

}
