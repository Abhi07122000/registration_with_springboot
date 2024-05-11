package com.example.CashrichLogin.api.v1.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdationDto {

	@NotBlank
	private String token;
	
	@NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$")
    private String password;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{10})")
    private String mobileNo;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,15}$")
    private String username;

}
