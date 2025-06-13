package ra.edu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class AdminDTO {
    private int id;
    @NotBlank(message = "username không được để trống")
    private String username;
    @NotBlank(message = "password không được để trống")
    private String password;
}
