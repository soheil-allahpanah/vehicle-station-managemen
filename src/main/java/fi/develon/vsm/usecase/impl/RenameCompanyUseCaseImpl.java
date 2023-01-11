package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.RenameCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.NewParentCompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import io.vavr.control.Try;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;
import java.util.Optional;

public class RenameCompanyUseCaseImpl implements RenameCompanyUseCase {

    CompanyRepository companyRepository;

    public RenameCompanyUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Try<RenameCompanyResponse> rename(RenameCompanyRequest request) {
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentifierNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException());
        }

        Optional<Company> companyByNameOpt = companyRepository.findByName(request.getNewName());
        if (companyByNameOpt.isPresent() &&
                !companyByNameOpt.get().getId().equals(companyByIdNumOpt.get().getId())) {
            return Try.failure(new DuplicateCompanyByNameException());
        }
        companyByIdNumOpt.get().setName(request.getNewName());
        Company updatedCompany = companyRepository.save(companyByIdNumOpt.get());
        return Try.success(RenameCompanyResponse.builder()
                .name(updatedCompany.getName())
                .identificationNumber(updatedCompany.getIdentificationNumber())
                .updatedAt(updatedCompany.getUpdatedAt())
                .build());
    }

}
