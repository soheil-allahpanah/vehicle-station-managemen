package fi.develon.vsm.adapter.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddStationToCompanyResDto {

    private String ownerId;
    private String ownerName;
    private Long stationId;
    private String stationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime updatedAt;

}
