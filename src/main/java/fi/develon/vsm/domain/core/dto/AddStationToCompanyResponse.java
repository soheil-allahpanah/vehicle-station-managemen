package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationName;

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
    private GeoLocation location;
    private LocalDateTime updatedAt;

}
