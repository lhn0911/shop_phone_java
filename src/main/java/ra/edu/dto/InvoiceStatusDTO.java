package ra.edu.dto;

import lombok.Data;
import ra.edu.utils.InvoiceStatus;

import javax.validation.constraints.NotNull;

@Data
public class InvoiceStatusDTO {
    private Integer id;

    @NotNull(message = "Trạng thái không được để trống")
    private InvoiceStatus status;
}
