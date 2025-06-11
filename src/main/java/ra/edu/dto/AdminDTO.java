package ra.edu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class AdminDTO {
    private int id;
    @NotBlank(message = "username không được để trống")
    @Size(min = 5, max = 50, message = "username phải từ 5 đến 50 ký tự")
    private String username;
    @NotBlank(message = "password không được để trống")
    private String password;
}
