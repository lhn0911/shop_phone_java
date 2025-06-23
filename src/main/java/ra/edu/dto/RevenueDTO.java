package ra.edu.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class RevenueDTO {
    private LocalDate date;
    private Integer month;
    private Integer year;
    private Double totalAmount;
}
