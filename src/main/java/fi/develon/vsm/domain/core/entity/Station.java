package fi.develon.vsm.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Station {

    StationId stationId;
    StationName name;
    CompanyId owner;
    GeoLocation location;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
