package fi.develon.vsm.usecase.impl.company;

import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.UnregisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("unregister company with identification number of {}, unregisterItsSubsidiary: {} "
                , request.getIdentificationNumber().value(), request.getUnregisterItsSubsidiary());
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            log.debug("company with identification number {} not found", request.getIdentificationNumber().value());
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }
        List<Company> subsidiaries = companyRepository.getCompaniesSubsidiaries(request.getIdentificationNumber()).stream()
                .filter(fc -> !fc.getId().equals(companyByIdNumOpt.get().getId())).toList();


        if (request.getUnregisterItsSubsidiary()) {
            companyRepository.deleteAll(subsidiaries);
            companyRepository.delete(companyByIdNumOpt.get());
            log.info("unregister all subsidiaries was true, so {} subsidiaries deleted", subsidiaries.size());
            return Try.success(UnregisterCompanyResponse.builder()
                    .identificationNumber(companyByIdNumOpt.get().getIdentificationNumber())
                    .unregisteredSubsidiaries(subsidiaries.stream().toList())
                    .build());
        } else {
            companyRepository.delete(companyByIdNumOpt.get());
            companyRepository.saveAll(subsidiaries.stream().filter(s -> s.getParent().equals(companyByIdNumOpt.get().getId())).peek(s -> s.setParent(null)).toList());
            log.info("unregister all subsidiaries was false, so main company is deleted and its direct subsidiaries parent become null");
            return Try.success(UnregisterCompanyResponse.builder()
                    .identificationNumber(companyByIdNumOpt.get().getIdentificationNumber())
                    .build());
        }
    }

}
