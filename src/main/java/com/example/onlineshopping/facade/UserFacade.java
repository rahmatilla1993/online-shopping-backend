package com.example.onlineshopping.facade;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserFacade {
    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;
    private List<String> roles;
    private String status;
}
