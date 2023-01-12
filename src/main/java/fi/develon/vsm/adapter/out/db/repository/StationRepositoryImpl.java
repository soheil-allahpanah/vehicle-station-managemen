package fi.develon.vsm.adapter.out.db.repository;

import fi.develon.vsm.adapter.out.db.mapper.StationRepositoryObjectMapper;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceResponse;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.domain.repository.StationRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


public class StationRepositoryImpl implements StationRepository {

    private final StationRepositoryJpaInterface stationRepositoryJpaInterface;
    private final StationRepositoryObjectMapper repositoryObjectMapper = new StationRepositoryObjectMapper();


    public StationRepositoryImpl(StationRepositoryJpaInterface stationRepositoryJpaInterface) {
        this.stationRepositoryJpaInterface = stationRepositoryJpaInterface;
    }


    @Override
    public Optional<Station> findById(StationId stationId) {
        return Optional.empty();
    }

    @Override
    public Station save(Station station) {
        return repositoryObjectMapper.toDomain(stationRepositoryJpaInterface.save(repositoryObjectMapper.toEntity(station)));
    }

    @Override
    public void deleteAll(List<Station> stations) {
        stationRepositoryJpaInterface.deleteAllInBatch(stations.stream().map(repositoryObjectMapper::toEntity).toList());
    }

    @Override
    public void delete(Station station) {
        stationRepositoryJpaInterface.delete(repositoryObjectMapper.toEntity(station));
    }

    @Override
    public void saveAll(List<Station> stations) {
        stationRepositoryJpaInterface.saveAll(stations.stream().map(repositoryObjectMapper::toEntity).toList());
    }

    @Override
    public Optional<Station> findByName(StationName name) {
        return stationRepositoryJpaInterface.findByName(name.name()).map(repositoryObjectMapper::toDomain);
    }

    @Override
    public Optional<Station> findByLocation(GeoLocation location) {
        return stationRepositoryJpaInterface.findByLongitudeAndLatitude(location.longitude(), location.latitude())
                .map(repositoryObjectMapper::toDomain);
    }

    //company_id, station_id, company_name,  station_name, latitude, longitude, identification_number, parent_id, distance, created_at, updated_at
    @Override
    public List<GetStationsWithDistanceResponse> getStationWithDistanceForGivenCompany(IdentificationNumber identificationNumber, GeoLocation location) {
        var result = stationRepositoryJpaInterface.getStationWithDistanceForGivenCompany(identificationNumber.value()
                , location.latitude(), location.longitude());
        return result.stream().map(r ->
                GetStationsWithDistanceResponse.builder()
                        .companyName(new CompanyName((String) r[2]))
                        .identificationNumber(new IdentificationNumber((String) r[6]))
                        .stationId(new StationId( ((BigInteger) r[1]).longValue()))
                        .stationName(new StationName((String) r[3]))
                        .location(new GeoLocation(BigDecimal.valueOf((Double) r[4]), BigDecimal.valueOf((Double) r[5])))
                        .distance(BigDecimal.valueOf((Double) r[8]))
                        .distanceByKm(BigDecimal.valueOf((Double) r[8]).compareTo(BigDecimal.ZERO) > 0 ?
                                BigDecimal.valueOf((Double) r[8]).divide(BigDecimal.valueOf(1000)) : BigDecimal.ZERO )
                        .build()
        ).toList();
    }
}
