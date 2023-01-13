package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddStationToCompanyResponse {

    private IdentificationNumber ownerId;
    private CompanyName ownerName;
    private StationName name;
    private StationId stationId;
    private GeoLocation location;
    private LocalDateTime updatedAt;

}
