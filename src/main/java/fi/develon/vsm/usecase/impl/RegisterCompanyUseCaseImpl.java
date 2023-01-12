package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.RegisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.RegisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import org.apache.logging.log4j.util.Strings;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class RegisterCompanyUseCaseImpl implements RegisterCompanyUseCase {

    CompanyRepository companyRepository;

    public RegisterCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Try<RegisterCompanyResponse> register(RegisterCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isPresent()) {
            return Try.failure(new DuplicateCompanyByIdentifierException("Company with given IdentificationNumber exist", 409));
        }
        Optional<Company> companyByNameOpt = companyRepository.findByName(request.getName());
        if (companyByNameOpt.isPresent()) {
            return Try.failure(new DuplicateCompanyByNameException("Company with given Name exist", 409));
        }
        Company parentCompany = null;
        if (Objects.nonNull(request.getParentIdentificationNumber()) && Objects.nonNull(request.getParentIdentificationNumber().value())
                && Strings.isNotBlank(request.getParentIdentificationNumber().value())) {
            Optional<Company> parentCompanyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getParentIdentificationNumber());
            if (parentCompanyByIdNumOpt.isEmpty()) {
                return Try.failure(new ParentCompanyNotRegisteredException("Parent Company with given ParentIdentificationNumber not found", 404));
            } else {
                parentCompany = parentCompanyByIdNumOpt.get();
            }
        }

        Company company = companyRepository.save(new Company(null, parentCompany == null ? null : parentCompany.getId()
                , request.getIdentificationNumber()
                , request.getName()
                , null
                , LocalDateTime.now()
                , LocalDateTime.now()));
        return Try.success(RegisterCompanyResponse.builder()
                .name(company.getName())
                .parentIdentificationNumber(parentCompany == null ? null : parentCompany.getIdentificationNumber())
                .parentName(parentCompany == null ? null : parentCompany.getName())
                .identificationNumber(company.getIdentificationNumber())
                .updatedAt(company.getUpdatedAt())
                .build());
    }

}
