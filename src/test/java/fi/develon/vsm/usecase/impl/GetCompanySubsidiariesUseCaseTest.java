package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesRequest;
import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesResponse;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.CompanyId;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.GetCompanySubsidiariesUseCase;
import fi.develon.vsm.usecase.company.UnregisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.impl.company.GetCompanySubsidiariesUseCaseImpl;
import fi.develon.vsm.usecase.impl.company.UnregisterCompanyUseCaseImpl;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;

public class GetCompanySubsidiariesUseCaseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private GetCompanySubsidiariesUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new GetCompanySubsidiariesUseCaseImpl(companyRepository);
    }


    @Test
    void givenGetCompanySubsidiariesRequestWhenCompanyNotExistShouldThrowCompanyNotRegisteredException() {
        GetCompanySubsidiariesRequest request = new GetCompanySubsidiariesRequest(new IdentificationNumber("1000"));
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        var response = useCase.getSubsidiaries(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenGetCompanySubsidiariesRequestWhenCompanyExistShouldProperResponse() {
        GetCompanySubsidiariesRequest request = new GetCompanySubsidiariesRequest(new IdentificationNumber("1000"));
        Company rootCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());

        Company childOne = new Company(new CompanyId(2L)
                , new CompanyId(1L)
                , new IdentificationNumber("10001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        Company childTwoOne = new Company(new CompanyId(3L)
                , new CompanyId(2L)
                , new IdentificationNumber("100011")
                , new CompanyName("c11"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(List.of(rootCompany, childOne, childTwoOne)).when(companyRepository).getCompaniesSubsidiaries(request.getIdentificationNumber());
        var response = useCase.getSubsidiaries(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getSubsidiaries().size(), equalTo(1));
        assertThat(response.get().getSubsidiaries().get(0).getSubsidiaries().size(), equalTo(1));
    }


}
