package fi.develon.vsm.domain.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import fi.develon.vsm.domain.core.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStationsWithDistanceResponse {
    private IdentificationNumber identificationNumber;
    private CompanyName companyName;
    private StationId stationId;
    private StationName stationName;
    private GeoLocation location;
    private BigDecimal distance;
    private BigDecimal distanceByKm;

}
