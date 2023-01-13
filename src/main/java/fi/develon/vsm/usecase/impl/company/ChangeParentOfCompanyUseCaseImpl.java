package fi.develon.vsm.usecase.impl.company;

import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.ChangeParentOfCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.NewParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
public class ChangeParentOfCompanyUseCaseImpl implements ChangeParentOfCompanyUseCase {

    CompanyRepository companyRepository;

    public ChangeParentOfCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Try<ChangeParentOfCompanyResponse> changeParent(ChangeParentOfCompanyRequest request) {
        log.info("change parent of {} to {}", request.getIdentificationNumber().value(), request.getNewParentIdentificationNumber().value());
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            log.debug("company of {} not found", request.getIdentificationNumber().value());
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }
        Optional<Company> newParentCompanyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getNewParentIdentificationNumber());
        log.debug("new parent company of {} not found", request.getNewParentIdentificationNumber().value());
        if (newParentCompanyByIdNumOpt.isEmpty()) {
            return Try.failure(new NewParentCompanyNotRegisteredException("Company with given newParentIdentificationNumber not found", 404));
        }

        companyByIdNumOpt.get().setParent(newParentCompanyByIdNumOpt.get().getId());
        Company updatedCompany = companyRepository.save(companyByIdNumOpt.get());
        log.info("change parent of {} to {} is done", companyByIdNumOpt.get().getName().value(), newParentCompanyByIdNumOpt.get().getName().value());
        return Try.success(ChangeParentOfCompanyResponse.builder()
                .name(updatedCompany.getName())
                .identificationNumber(updatedCompany.getIdentificationNumber())
                .parentName(newParentCompanyByIdNumOpt.get().getName())
                .parentIdentificationNumber(newParentCompanyByIdNumOpt.get().getIdentificationNumber())
                .updatedAt(updatedCompany.getUpdatedAt())
                .build());
    }

}
