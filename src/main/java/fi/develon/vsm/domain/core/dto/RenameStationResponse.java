package fi.develon.vsm.domain.core.dto;

import fi.develon.vsm.domain.core.entity.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameStationResponse {

    private StationId id;
    private StationName newName;

}
