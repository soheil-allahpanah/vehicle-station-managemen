package fi.develon.vsm.adapter.out.db.repository;

import fi.develon.vsm.adapter.out.db.entity.CompanyEntity;
import fi.develon.vsm.adapter.out.db.mapper.CompanyRepositoryObjectMapper;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;


public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyRepositoryJpaInterface repositoryJpaInterface;
    private final CompanyRepositoryObjectMapper repositoryObjectMapper = new CompanyRepositoryObjectMapper();


    public CompanyRepositoryImpl(CompanyRepositoryJpaInterface repositoryJpaInterface) {
        this.repositoryJpaInterface = repositoryJpaInterface;
    }

    @Override
    public Optional<Company> findByIdentificationNumber(IdentificationNumber idNumber) {
        Optional<CompanyEntity> companyEntityOpt = repositoryJpaInterface.findByIdentificationNumber(idNumber.value());
        return companyEntityOpt.map(repositoryObjectMapper::toDomain);
    }

    @Override
    public Optional<Company> findByName(CompanyName name) {
        Optional<CompanyEntity> companyEntityOpt = repositoryJpaInterface.findByName(name.value());
        return companyEntityOpt.map(repositoryObjectMapper::toDomain);
    }

    @Override
    public Company save(Company company) {
        return repositoryObjectMapper.toDomain(repositoryJpaInterface.save(repositoryObjectMapper.toEntity(company)));
    }

    @Override
    public List<Company> getCompaniesSubsidiaries(IdentificationNumber idNum) {
        List<CompanyEntity> companyEntities = repositoryJpaInterface.getCompaniesSubsidiaries(idNum.value());
        return companyEntities.stream().map(repositoryObjectMapper::toDomain).toList();
    }

    @Override
    public void deleteAll(List<Company> companies) {
        var entities = companies.stream().map(repositoryObjectMapper::toEntity).toList();
        repositoryJpaInterface.deleteAllInBatch(entities);
    }

    @Override
    public void delete(Company company) {
        repositoryJpaInterface.delete(repositoryObjectMapper.toEntity(company));
    }

    @Override
    public void saveAll(List<Company> companies) {
        var entities = companies.stream().map(repositoryObjectMapper::toEntity).toList();
        repositoryJpaInterface.saveAll(entities);
    }
}
