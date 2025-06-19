package ra.edu.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ra.edu.utils.InvoiceStatus;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class InvoiceDTO {

    private Integer id;

    @NotNull(message = "Khách hàng không được để trống")
    private Integer customerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    private String formattedDate;

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tổng tiền phải lớn hơn 0")
    private BigDecimal totalAmount;

    private InvoiceStatus status;

    private List<InvoiceDetailDTO> invoiceDetails = new ArrayList<>();
}
