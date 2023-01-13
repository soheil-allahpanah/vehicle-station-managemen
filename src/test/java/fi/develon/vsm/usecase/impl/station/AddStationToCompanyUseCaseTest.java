package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyResponse;
import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.company.RegisterCompanyUseCase;
import fi.develon.vsm.usecase.exception.*;
import fi.develon.vsm.usecase.impl.company.RegisterCompanyUseCaseImpl;
import fi.develon.vsm.usecase.station.AddStationToCompanyUseCase;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddStationToCompanyUseCaseTest {

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    private final StationRepository stationRepository = Mockito.mock(StationRepository.class);
    private AddStationToCompanyUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new AddStationToCompanyUseCaseImpl(companyRepository, stationRepository);
    }


    @Test
    void givenAddStationToCompanyRequestWhenOwnerNotFoundShouldThrowCompanyNotRegisteredException() {
        AddStationToCompanyRequest request = new AddStationToCompanyRequest(new IdentificationNumber("1000"), new StationName("st1")
                , new GeoLocation(BigDecimal.valueOf(27.2137)
                , BigDecimal.valueOf(2.4671)));
        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getOwner());
        var response = useCase.addStation(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenAddStationToCompanyRequestWhenNameIsDuplicatedShouldThrowCompanyNotRegisteredException() {
        AddStationToCompanyRequest request = new AddStationToCompanyRequest(new IdentificationNumber("1000"), new StationName("st1")
                , new GeoLocation(BigDecimal.valueOf(27.2137)
                , BigDecimal.valueOf(2.4671)));
        Company rootCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(new GeoLocation(BigDecimal.valueOf(27.2138), BigDecimal.valueOf(2.4677)))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.of(station)).when(stationRepository).findByName(request.getName());
        var response = useCase.addStation(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateStationByNameException.class));
    }

    @Test
    void givenAddStationToCompanyRequestWhenLocationIsDuplicatedShouldThrowCompanyNotRegisteredException() {
        var location = new GeoLocation(BigDecimal.valueOf(27.2137), BigDecimal.valueOf(2.4671));
        AddStationToCompanyRequest request = new AddStationToCompanyRequest(new IdentificationNumber("1000"), new StationName("st1"), location);
        Company rootCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(location)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.empty()).when(stationRepository).findByName(request.getName());
        doReturn(Optional.of(station)).when(stationRepository).findByLocation(request.getLocation());
        var response = useCase.addStation(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateStationByLocationException.class));
    }

    @Test
    void givenAddStationToCompanyRequestWhenEveryThingIsOkShouldReturnProperResponse() {
        var location = new GeoLocation(BigDecimal.valueOf(27.2137), BigDecimal.valueOf(2.4671));
        AddStationToCompanyRequest request = new AddStationToCompanyRequest(new IdentificationNumber("1000"), new StationName("st1"), location);
        Company rootCompany = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(location)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        var expectedResponse = AddStationToCompanyResponse.builder()
                .stationId(station.getStationId())
                .owner(rootCompany.getIdentificationNumber())
                .ownerName(rootCompany.getName())
                .name(station.getName())
                .updatedAt(station.getUpdatedAt())
                .location(station.getLocation())
                .build();

        doReturn(Optional.of(rootCompany)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.empty()).when(stationRepository).findByName(request.getName());
        doReturn(Optional.empty()).when(stationRepository).findByLocation(request.getLocation());
        doReturn(station).when(stationRepository).save(any(Station.class));
        var response = useCase.addStation(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get(), equalTo(expectedResponse));
    }

}
