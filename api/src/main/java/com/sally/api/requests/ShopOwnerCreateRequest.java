package com.sally.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOwnerCreateRequest {
    private String username;
    private String password;
    private String companyName;
}
