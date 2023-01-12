package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.UnregisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import io.vavr.control.Try;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class UnregisterCompanyUseCaseImpl implements UnregisterCompanyUseCase {

    CompanyRepository companyRepository;

    public UnregisterCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private List<Company> fillSubsidiaries(Company company, List<Company> companies) {
        List<Company> subsidiaries = companies.stream().filter(c -> c.getParent() != null && c.getParent().value().equals(company.getId().value())).toList();
        for (Company sub : subsidiaries) {
            sub.setSubsidiaries(fillSubsidiaries(sub, companies));
        }
        return subsidiaries;
    }

    @Transactional
    public Try<UnregisterCompanyResponse> unregister(UnregisterCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }
        List<Company> subsidiaries = companyRepository.getCompaniesSubsidiaries(request.getIdentificationNumber()).stream()
                .filter(fc -> !fc.getId().equals(companyByIdNumOpt.get().getId())).toList();


        if (request.getUnregisterItsSubsidiary()) {
            companyRepository.deleteAll(subsidiaries);
            companyRepository.delete(companyByIdNumOpt.get());
            return Try.success(UnregisterCompanyResponse.builder()
                    .identificationNumber(companyByIdNumOpt.get().getIdentificationNumber())
                    .unregisteredSubsidiaries(subsidiaries.stream().toList())
                    .build());
        } else {
            companyRepository.delete(companyByIdNumOpt.get());
            companyRepository.saveAll(subsidiaries.stream().filter(s -> s.getParent().equals(companyByIdNumOpt.get().getId())).peek(s -> s.setParent(null)).toList());
            return Try.success(UnregisterCompanyResponse.builder()
                    .identificationNumber(companyByIdNumOpt.get().getIdentificationNumber())
                    .build());
        }
    }

}
