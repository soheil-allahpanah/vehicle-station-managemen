package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationId;
import fi.develon.vsm.domain.core.entity.StationName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameStationRequest {

    private StationId id;
    private StationName newName;

}
