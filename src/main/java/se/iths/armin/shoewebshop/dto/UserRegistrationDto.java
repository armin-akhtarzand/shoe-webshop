package se.iths.armin.shoewebshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank(message = "E-post får inte vara tom")
    @Email(message = "Ange en giltig e-postadress")
    private String email;

    @NotBlank(message = "Lösenord får inte vara tomt")
    private String password;

    @AssertTrue(message = "Du måste godkänna behandlingen av personuppgifter")
    private boolean consent;
}
