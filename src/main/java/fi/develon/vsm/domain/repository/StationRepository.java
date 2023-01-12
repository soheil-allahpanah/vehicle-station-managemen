package fi.develon.vsm.domain.repository;

import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceResponse;
import fi.develon.vsm.domain.core.entity.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StationRepository {


    Optional<Station> findById(StationId stationId);

    Station save(Station station);

    void deleteAll(List<Station> stations);

    void delete (Station station);

    void saveAll(List<Station> stations);

    Optional<Station> findByName(StationName name);

    Optional<Station> findByLocation(GeoLocation location);

    List<GetStationsWithDistanceResponse> getStationWithDistanceForGivenCompany(IdentificationNumber identificationNumber, GeoLocation location);
}
