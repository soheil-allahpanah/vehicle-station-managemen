package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.ChangeParentOfCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.NewParentCompanyNotRegisteredException;
import io.vavr.control.Try;

import javax.transaction.Transactional;
import java.util.Optional;

public class ChangeParentOfCompanyUseCaseImpl implements ChangeParentOfCompanyUseCase {

    CompanyRepository companyRepository;

    public ChangeParentOfCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Try<ChangeParentOfCompanyResponse> changeParent(ChangeParentOfCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }
        Optional<Company> newParentCompanyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getNewParentIdentificationNumber());
        if (newParentCompanyByIdNumOpt.isEmpty()) {
            return Try.failure(new NewParentCompanyNotRegisteredException("Company with given newParentIdentificationNumber not found", 404));
        }

        companyByIdNumOpt.get().setParent(newParentCompanyByIdNumOpt.get().getId());
        Company updatedCompany = companyRepository.save(companyByIdNumOpt.get());
        return Try.success(ChangeParentOfCompanyResponse.builder()
                .name(updatedCompany.getName())
                .identificationNumber(updatedCompany.getIdentificationNumber())
                .parentName(newParentCompanyByIdNumOpt.get().getName())
                .parentIdentificationNumber(newParentCompanyByIdNumOpt.get().getIdentificationNumber())
                .updatedAt(updatedCompany.getUpdatedAt())
                .build());
    }

}
