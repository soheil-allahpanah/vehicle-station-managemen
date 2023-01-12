package fi.develon.vsm.domain.repository;

import fi.develon.vsm.domain.core.entity.*;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> findByIdentificationNumber(IdentificationNumber idNumber);

    Optional<Company> findByName(CompanyName name);

    Company save(Company company);

    List<Company> getCompaniesSubsidiaries(IdentificationNumber identificationNumber);

    void deleteAll(List<Company> company);

    void delete (Company company);

    void saveAll(List<Company> companies);

}
