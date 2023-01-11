package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.RegisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.CompanyId;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.RegisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class RegisterCompanyUseCaseImpl implements RegisterCompanyUseCase {

    CompanyRepository companyRepository;

    public RegisterCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Try<RegisterCompanyResponse> register(RegisterCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentifierNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isPresent()) {
            return Try.failure(new DuplicateCompanyByIdentifierException());
        }
        Optional<Company> companyByNameOpt = companyRepository.findByName(request.getName());
        if (companyByNameOpt.isPresent()) {
            return Try.failure(new DuplicateCompanyByNameException());
        }
        Company parentCompany = null;
        if (Objects.nonNull(request.getParentIdentificationNumber()) && Objects.nonNull(request.getParentIdentificationNumber().value())
                && Strings.isNotBlank(request.getParentIdentificationNumber().value())) {
            Optional<Company> parentCompanyByIdNumOpt = companyRepository.findByIdentifierNumber(request.getParentIdentificationNumber());
            if (parentCompanyByIdNumOpt.isEmpty()) {
                return Try.failure(new ParentCompanyNotRegisteredException());
            } else {
                parentCompany = parentCompanyByIdNumOpt.get();
            }
        }

        Company company = companyRepository.save(new Company(null, parentCompany == null ? null : parentCompany.id()
                , request.getIdentificationNumber()
                , request.getName()
                , null
                , LocalDateTime.now()
                , LocalDateTime.now()));
        return Try.success(RegisterCompanyResponse.builder()
                .name(company.name())
                .parentIdentificationNumber(parentCompany == null ? null : parentCompany.identificationNumber())
                .parentName(parentCompany == null ? null : parentCompany.name())
                .identificationNumber(company.identificationNumber())
                .updatedAt(company.updatedAt())
                .build());
    }

}
