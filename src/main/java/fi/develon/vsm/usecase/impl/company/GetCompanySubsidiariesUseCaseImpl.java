package fi.develon.vsm.usecase.impl.company;

import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesRequest;
import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.GetCompanySubsidiariesUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class GetCompanySubsidiariesUseCaseImpl implements GetCompanySubsidiariesUseCase {

    CompanyRepository companyRepository;

    public GetCompanySubsidiariesUseCaseImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private List<Company> fillSubsidiaries(Company company, List<Company> companies) {
        List<Company> subsidiaries = companies.stream().filter(c -> c.getParent() != null &&  c.getParent().value().equals(company.getId().value())).toList();
        for(Company sub: subsidiaries) {
            sub.setSubsidiaries(fillSubsidiaries(sub, companies));
        }
        return subsidiaries;
    }

    private GetCompanySubsidiariesResponse toResponse(Company company) {
        return GetCompanySubsidiariesResponse.builder()
                .name(company.getName())
                .identificationNumber(company.getIdentificationNumber())
                .subsidiaries(company.getSubsidiaries().stream().map(this::toResponse).toList())
                .build();
    }

    public Try<GetCompanySubsidiariesResponse> getSubsidiaries(GetCompanySubsidiariesRequest request) {
        log.info("get all subsidiaries of {} ", request.getIdentificationNumber().value());
        Optional<Company> companyByIdNumOpt = companyRepository.findByIdentificationNumber(request.getIdentificationNumber());
        if (companyByIdNumOpt.isEmpty()) {
            log.debug("company of {} not found", request.getIdentificationNumber().value());
            return Try.failure(new CompanyNotRegisteredException("Company with given identificationNumber not found", 404));
        }
        List<Company> flattenCompany = companyRepository.getCompaniesSubsidiaries(request.getIdentificationNumber());
        flattenCompany = flattenCompany.stream().filter(fc -> fc.getId() != companyByIdNumOpt.get().getId()).toList();

        List<Company> subsidiaries = fillSubsidiaries(companyByIdNumOpt.get(), flattenCompany);
        companyByIdNumOpt.get().setSubsidiaries(subsidiaries);
        log.info("returning all subsidiaries of {} ", companyByIdNumOpt.get().getName());
        return Try.success(toResponse(companyByIdNumOpt.get()));
    }

}
