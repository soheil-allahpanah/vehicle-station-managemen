package fi.develon.vsm.adapter.out.db.repository;

import fi.develon.vsm.adapter.out.db.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepositoryJpaInterface extends JpaRepository<CompanyEntity, Long>  {

    Optional<CompanyEntity> findByIdentificationNumber(String idNumber);

    Optional<CompanyEntity> findByName(String name);

    @Query(value = "select * from get_companies_subsidiaries(:identification_number);", nativeQuery = true)
    List<CompanyEntity> getCompaniesSubsidiaries(@Param("identification_number") String value);
}
