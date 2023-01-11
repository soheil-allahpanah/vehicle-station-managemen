package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.ChangeParentOfCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.NewParentCompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;
import java.util.Optional;

public class ChangeParentOfCompanyUseCaseImpl implements ChangeParentOfCompanyUseCase {

    CompanyRepository companyRepository;

    public ChangeParentOfCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Try<ChangeParentOfCompanyResponse> changeParent(ChangeParentOfCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentifierNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException());
        }
        Optional<Company> newParentCompanyByIdNumOpt = companyRepository.findByIdentifierNumber(request.getNewParentIdentificationNumber());
        if (newParentCompanyByIdNumOpt.isEmpty()) {
            return Try.failure(new NewParentCompanyNotRegisteredException());
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
