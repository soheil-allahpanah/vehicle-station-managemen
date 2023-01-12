package fi.develon.vsm.adapter.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStationsWithDistanceResDto {
    private String identificationNumber;
    private String companyName;
    private Long stationId;
    private String stationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal distance;
    private BigDecimal distanceInKm;


}
