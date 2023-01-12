package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.RegisterCompanyUseCase;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByIdentifierException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.ParentCompanyNotRegisteredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class RegisterCompanyUseCaseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private RegisterCompanyUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new RegisterCompanyUseCaseImpl(companyRepository);
    }

    @Test
    void givenRegisterRequestWhenAlreadyRegisteredByIdentifierShouldReturnDuplicateCompanyByIdentifierException() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c0"), new IdentificationNumber("1000"), null);
        Company registeredCompany = new Company(new CompanyId(1L), null, new IdentificationNumber("1000"), new CompanyName("c1"), null, LocalDateTime.now(), LocalDateTime.now());
        assertThat(registeredCompany.getIdentificationNumber(), equalTo(request.getIdentificationNumber()));
        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        var response = useCase.register(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateCompanyByIdentifierException.class));
    }

    @Test
    void givenRegisterRequestWhenAlreadyRegisteredByNameShouldReturnDuplicateCompanyByNameException() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c0"), new IdentificationNumber("1000"), null);
        Company registeredCompany = new Company(new CompanyId(1L), null, new IdentificationNumber("2000"), new CompanyName("c0"), null, LocalDateTime.now(), LocalDateTime.now());
        assertThat(registeredCompany.getIdentificationNumber(), not(equalTo(request.getIdentificationNumber())));
        assertThat(registeredCompany.getName(), equalTo(request.getName()));
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByName(request.getName());
        var response = useCase.register(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateCompanyByNameException.class));
    }

    @Test
    void givenRegisterRequestWhenParentExistButNotRegisteredShouldReturnParentNotRegisteredException() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c1"), new IdentificationNumber("1001"), new IdentificationNumber("1000"));
        Company parentCompany = new Company(new CompanyId(0L), null, new IdentificationNumber("1000"), new CompanyName("c0"), null, LocalDateTime.now(), LocalDateTime.now());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByName(request.getName());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getParentIdentificationNumber());
        var response = useCase.register(request);
        verify((companyRepository), times(2)).findByIdentificationNumber(any(IdentificationNumber.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(ParentCompanyNotRegisteredException.class));
    }

    @Test
    void givenRegisterRequestWhenParentNotExistShouldNotCheckParentExistence() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c1"), new IdentificationNumber("1001"), null);
        Company registeredCompany = new Company(new CompanyId(1L), null, new IdentificationNumber("1001"), new CompanyName("c1"), null, LocalDateTime.now(), LocalDateTime.now());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByName(request.getName());
        doReturn(registeredCompany).when(companyRepository).save(any(Company.class));
        useCase.register(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));
    }


    @Test
    void givenRegisterRequestWithOutParentWhenItsNotRegisteredShouldReturnRegisterCompany() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c0"), new IdentificationNumber("1000"), null);
        Company registeredCompany = new Company(new CompanyId(1L), null, new IdentificationNumber("1000"), new CompanyName("c0"), null, LocalDateTime.now(), LocalDateTime.now());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByName(request.getName());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getParentIdentificationNumber());
        doReturn(registeredCompany).when(companyRepository).save(any(Company.class));
        var response = useCase.register(request);

        verify((companyRepository), times(1)).save(any(Company.class));
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(request.getIdentificationNumber(), equalTo(response.get().getIdentificationNumber()));
        assertThat(request.getParentIdentificationNumber(), equalTo(response.get().getParentIdentificationNumber()));
        assertThat(request.getName(), equalTo(response.get().getName()));
        assertThat(response.get().getUpdatedAt(), is(notNullValue()));

    }

    @Test
    void givenRegisterRequestWithRegisteredParentWhenItsNotRegisteredShouldReturnRegisterCompany() {
        RegisterCompanyRequest request = new RegisterCompanyRequest(new CompanyName("c1"), new IdentificationNumber("1001"), new IdentificationNumber("1000"));
        Company parentCompany = new Company(new CompanyId(1L), null, new IdentificationNumber("1000"), new CompanyName("c0"), null, LocalDateTime.now(), LocalDateTime.now());
        Company registeredCompany = new Company(new CompanyId(2L), null, new IdentificationNumber("1001"), new CompanyName("c1"), null, LocalDateTime.now(), LocalDateTime.now());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByName(request.getName());
        doReturn(Optional.of(parentCompany)).when(companyRepository).findByIdentificationNumber(request.getParentIdentificationNumber());
        doReturn(registeredCompany).when(companyRepository).save(any(Company.class));
        var response = useCase.register(request);

        verify((companyRepository), times(1)).save(any(Company.class));
        verify((companyRepository), times(2)).findByIdentificationNumber(any(IdentificationNumber.class));
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(request.getName(), equalTo(response.get().getName()));
        assertThat(request.getIdentificationNumber(), equalTo(response.get().getIdentificationNumber()));

        assertThat(request.getParentIdentificationNumber(), equalTo(response.get().getParentIdentificationNumber()));
        assertThat(parentCompany.getIdentificationNumber(), equalTo(response.get().getParentIdentificationNumber()));
        assertThat(parentCompany.getName(), equalTo(response.get().getParentName()));
        assertThat(response.get().getUpdatedAt(), is(notNullValue()));
    }
}
