package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.StationId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelocateStationRequest {

    private StationId id;
    private GeoLocation newLocation;
}
