package ra.edu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerDTO {
    private int id;
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String name;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    private String email;
    private String address;
    private boolean isDeleted = false;
}
