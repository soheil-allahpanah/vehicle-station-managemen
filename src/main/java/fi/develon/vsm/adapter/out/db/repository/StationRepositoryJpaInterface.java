package fi.develon.vsm.adapter.out.db.repository;

import fi.develon.vsm.adapter.out.db.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface StationRepositoryJpaInterface extends JpaRepository<StationEntity, Long>  {


    Optional<StationEntity> findByName(String name);

    Optional<StationEntity> findByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);

    @Query(value = "select * from get_station_with_distance(:identification_number, :latitude , :longitude)", nativeQuery = true)
    List<Object[]> getStationWithDistanceForGivenCompany(@Param("identification_number") String identificationNumber
            , @Param("latitude") BigDecimal latitude
            , @Param("longitude") BigDecimal longitude );
}
