package com.tecnocampus.LS2.protube_back.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private long subscriptionCount;
    private List<String> subscribedChannels;
}
