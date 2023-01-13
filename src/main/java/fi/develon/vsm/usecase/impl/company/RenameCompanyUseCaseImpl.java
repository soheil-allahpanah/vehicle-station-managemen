package fi.develon.vsm.usecase.impl.company;

import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
public class RenameCompanyUseCaseImpl implements RenameCompanyUseCase {

    CompanyRepository companyRepository;

    public RenameCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Try<RenameCompanyResponse> rename(RenameCompanyRequest request) {
        log.info("rename of {} to {}", request.getIdentificationNumber().value(), request.getNewName().value());
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            log.debug("company with identification number {} not found", request.getIdentificationNumber().value());
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }

        Optional<Company> companyByNameOpt = companyRepository.findByName(request.getNewName());
        if (companyByNameOpt.isPresent() &&
                !companyByNameOpt.get().getId().equals(companyByIdNumOpt.get().getId())) {
            log.debug("company with name {} exist by requested  name ", request.getNewName().value());
            return Try.failure(new DuplicateCompanyByNameException("Chosen name is duplicated", 409));
        }
        companyByIdNumOpt.get().setName(request.getNewName());
        Company updatedCompany = companyRepository.save(companyByIdNumOpt.get());
        log.info("rename is done");
        return Try.success(RenameCompanyResponse.builder()
                .name(updatedCompany.getName())
                .identificationNumber(updatedCompany.getIdentificationNumber())
                .updatedAt(updatedCompany.getUpdatedAt())
                .build());
    }

}
