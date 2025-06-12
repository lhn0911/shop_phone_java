package ra.edu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private int id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm tối đa 100 ký tự")
    private String name;

    @NotBlank(message = "Nhãn hàng không được để trống")
    @Size(max = 50, message = "Nhãn hàng tối đa 50 ký tự")
    private String brand;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.01", inclusive = true, message = "Giá phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Giá không hợp lệ")
    private Double price;

    @NotNull(message = "Tồn kho không được để trống")
    @Min(value = 0, message = "Tồn kho không được âm")
    private Integer stock;

    @NotBlank(message = "Ảnh không được để trống")
    @Size(max = 255, message = "Đường dẫn ảnh tối đa 255 ký tự")
    private String image;
}
