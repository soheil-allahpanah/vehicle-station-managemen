package fi.develon.vsm.usecase.impl.company;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.RegisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.RegisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RegisterCompanyUseCaseImpl implements RegisterCompanyUseCase {

    CompanyRepository companyRepository;

    public RegisterCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Try<RegisterCompanyResponse> register(RegisterCompanyRequest request) {
        log.info("register company");
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isPresent()) {
            log.debug("company with name {} exist by requested identification number", companyByIdNumOpt.get().getName().value());
            return Try.failure(new DuplicateCompanyByIdentifierException("Company with given IdentificationNumber exist", 409));
        }
        Optional<Company> companyByNameOpt = companyRepository.findByName(request.getName());
        if (companyByNameOpt.isPresent()) {
            log.debug("company with name {} exist by requested  name ", companyByNameOpt.get().getName().value());
            return Try.failure(new DuplicateCompanyByNameException("Company with given Name exist", 409));
        }
        Company parentCompany = null;
        if (Objects.nonNull(request.getParentIdentificationNumber()) && Objects.nonNull(request.getParentIdentificationNumber().value())
                && Strings.isNotBlank(request.getParentIdentificationNumber().value())) {
            Optional<Company> parentCompanyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getParentIdentificationNumber());
            if (parentCompanyByIdNumOpt.isEmpty()) {
                log.debug("patent company with identification number {}, not registered ", request.getIdentificationNumber().value());
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
        log.debug("company registered {}", request.getIdentificationNumber().value());
        return Try.success(RegisterCompanyResponse.builder()
                .name(company.getName())
                .parentIdentificationNumber(parentCompany == null ? null : parentCompany.getIdentificationNumber())
                .parentName(parentCompany == null ? null : parentCompany.getName())
                .identificationNumber(company.getIdentificationNumber())
                .updatedAt(company.getUpdatedAt())
                .build());
    }

}
