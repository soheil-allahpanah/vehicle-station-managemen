package fi.develon.vsm.adapter.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelocateStationResDto {

    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
