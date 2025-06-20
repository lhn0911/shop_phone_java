package ra.edu.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class InvoiceDetailDTO {
    private int id;
    private int invoiceId;
    private int productId;
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int quantity;
    private BigDecimal unitPrice;

    private BigDecimal total;

    public void calculateTotal() {
        if (unitPrice != null && quantity > 0) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.total = BigDecimal.ZERO;
        }
    }
}