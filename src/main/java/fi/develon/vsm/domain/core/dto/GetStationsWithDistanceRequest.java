package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStationsWithDistanceRequest {
    private IdentificationNumber identificationNumber;
    private GeoLocation location;
    private Long radius;
}
