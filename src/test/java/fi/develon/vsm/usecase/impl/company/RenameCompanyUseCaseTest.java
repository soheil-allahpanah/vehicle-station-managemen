package fi.develon.vsm.usecase.impl.company;


import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.exception.*;
import fi.develon.vsm.usecase.impl.company.RenameCompanyUseCaseImpl;
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

public class RenameCompanyUseCaseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private RenameCompanyUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new RenameCompanyUseCaseImpl(companyRepository);
    }

    @Test
    void givenRenameCompanyWhenNoMatchFoundForIdNumShouldReturnCompanyNotFoundException() {
        RenameCompanyRequest request = new RenameCompanyRequest( new IdentificationNumber("1001")
                , new CompanyName("c01")
        );
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        var response = useCase.rename(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenRenameCompanyWhenNewIsFoundForOtherCompanyShouldReturnDuplicateCompanyByNameException() {
        RenameCompanyRequest request = new RenameCompanyRequest( new IdentificationNumber("1001")
                , new CompanyName("c01")
        );
        Company registeredCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        Company duplicateCompany = new Company(new CompanyId(2L)
                , null
                , new IdentificationNumber("1002")
                , new CompanyName("c01"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.of(duplicateCompany)).when(companyRepository).findByName(request.getNewName());

        var response = useCase.rename(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));
        verify((companyRepository), times(1)).findByName(any(CompanyName.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateCompanyByNameException.class));
    }


    @Test
    void givenRenameCompanyWhenNewNameIsFoundForTheCompanyShouldWorkProperly() {
        RenameCompanyRequest request = new RenameCompanyRequest( new IdentificationNumber("1001")
                , new CompanyName("c1")
        );
        Company registeredCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        Company currentCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.of(currentCompany)).when(companyRepository).findByName(request.getNewName());
        doReturn(currentCompany).when(companyRepository).save(any(Company.class));

        var response = useCase.rename(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));
        verify((companyRepository), times(1)).findByName(any(CompanyName.class));
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(request.getIdentificationNumber(), equalTo(response.get().getIdentificationNumber()));
        assertThat(request.getNewName(), equalTo(response.get().getName()));
        assertThat(response.get().getUpdatedAt(), is(notNullValue()));

    }

    @Test
    void givenRenameCompanyWhenNewNameIsNotFoundForTheCompanyShouldWorkProperly() {
        RenameCompanyRequest request = new RenameCompanyRequest( new IdentificationNumber("1001")
                , new CompanyName("c01")
        );
        Company registeredCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());

        Company updatedCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c01"), null
                , LocalDateTime.now()
                , LocalDateTime.now());

        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByName(request.getNewName());
        doReturn(updatedCompany).when(companyRepository).save(any(Company.class));

        var response = useCase.rename(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));


        assertThat(response.isSuccess(), equalTo(true));
        assertThat(request.getIdentificationNumber(), equalTo(response.get().getIdentificationNumber()));
        assertThat(request.getNewName(), equalTo(response.get().getName()));
        assertThat(response.get().getUpdatedAt(), is(notNullValue()));
    }
}
