package fi.develon.vsm.usecase.impl.company;


import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.company.ChangeParentOfCompanyUseCase;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.usecase.exception.*;
import fi.develon.vsm.usecase.impl.company.ChangeParentOfCompanyUseCaseImpl;
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

public class ChangeParentOfCompanyUseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private ChangeParentOfCompanyUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new ChangeParentOfCompanyUseCaseImpl(companyRepository);
    }


    @Test
    void givenChangeParentWhenNoMatchFoundForIdNumShouldReturnCompanyNotFoundException() {
        ChangeParentOfCompanyRequest request = new ChangeParentOfCompanyRequest( new IdentificationNumber("1001")
                , new IdentificationNumber("1000")
                );
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        var response = useCase.changeParent(request);
        verify((companyRepository), times(1)).findByIdentificationNumber(any(IdentificationNumber.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenChangeParentWhenNewParentNotFoundShouldReturnParentCompanyNotFoundException() {
        ChangeParentOfCompanyRequest request = new ChangeParentOfCompanyRequest( new IdentificationNumber("1001")
                , new IdentificationNumber("1000")
        );
        Company registeredCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getNewParentIdentificationNumber());

        var response = useCase.changeParent(request);
        verify((companyRepository), times(2)).findByIdentificationNumber(any(IdentificationNumber.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(NewParentCompanyNotRegisteredException.class));
    }


    @Test
    void givenChangeParentWhenEveryThingIsThereShouldChangeParent() {
        ChangeParentOfCompanyRequest request = new ChangeParentOfCompanyRequest( new IdentificationNumber("1001")
                , new IdentificationNumber("1000")
        );
        Company registeredCompany = new Company(new CompanyId(1L)
                , new CompanyId(2L)
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        Company newParentCompany = new Company(new CompanyId(3L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        Company updatedCompany = new Company(new CompanyId(1L)
                , new CompanyId(3L)
                , new IdentificationNumber("1001")
                , new CompanyName("c1"), null
                , LocalDateTime.now()
                , LocalDateTime.now());

        doReturn(Optional.of(registeredCompany)).when(companyRepository).findByIdentificationNumber(request.getIdentificationNumber());
        doReturn(Optional.of(newParentCompany)).when(companyRepository).findByIdentificationNumber(request.getNewParentIdentificationNumber());

        doReturn(updatedCompany).when(companyRepository).save(any(Company.class));
        var response = useCase.changeParent(request);
        verify((companyRepository), times(2)).findByIdentificationNumber(any(IdentificationNumber.class));


        assertThat(response.isSuccess(), equalTo(true));
        assertThat(request.getIdentificationNumber(), equalTo(response.get().getIdentificationNumber()));
        assertThat(request.getNewParentIdentificationNumber(), equalTo(response.get().getParentIdentificationNumber()));
        assertThat(response.get().getUpdatedAt(), is(notNullValue()));
    }
}
