package com.example.onlineshopping.facade;

import com.example.onlineshopping.enums.RoleName;
import lombok.Builder;

import java.util.List;

@Builder
public class UserFacade {
    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;
    private List<RoleName> roles;
    private String status;
}
