package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.CompanyId;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.RegisterCompanyUseCase;
import fi.develon.vsm.usecase.company.UnregisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import fi.develon.vsm.usecase.impl.company.RegisterCompanyUseCaseImpl;
import fi.develon.vsm.usecase.impl.company.UnregisterCompanyUseCaseImpl;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UnRegisterCompanyUseCaseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private UnregisterCompanyUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new UnregisterCompanyUseCaseImpl(companyRepository);
    }



    @Test
    void givenUnregisterCompanyRequestWhenCompanyNotFoundShouldReturnCompanyNotRegisteredException() {
        UnregisterCompanyRequest request = new UnregisterCompanyRequest(new IdentificationNumber("1000"), false);
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        var response = useCase.unregister(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenUnregisterCompanyRequestWhenUnRegisterSubsidiariesIsFalseReturnResponseWithOutSubsidiaries() {
        UnregisterCompanyRequest request = new UnregisterCompanyRequest(new IdentificationNumber("1000"), false);
        Company rootCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(List.of(rootCompany)).when(companyRepository).getCompaniesSubsidiaries(request.getIdentificationNumber());
        var response = useCase.unregister(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getIdentificationNumber(), equalTo(request.getIdentificationNumber()));
        assertThat(response.get().getUnregisteredSubsidiaries(), is(nullValue()));
    }

    @Test
    void givenUnregisterCompanyRequestWhenUnRegisterSubsidiariesIsTrueReturnResponseWithSubsidiaries() {
        UnregisterCompanyRequest request = new UnregisterCompanyRequest(new IdentificationNumber("1000"), true);
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
        Company childTwoOne = new Company(new CompanyId(2L)
                , new CompanyId(2L)
                , new IdentificationNumber("100011")
                , new CompanyName("c11"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(List.of(rootCompany, childOne, childTwoOne)).when(companyRepository).getCompaniesSubsidiaries(request.getIdentificationNumber());

        var response = useCase.unregister(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getIdentificationNumber(), equalTo(request.getIdentificationNumber()));
        assertThat(response.get().getUnregisteredSubsidiaries().size(), equalTo(2));
    }

}
