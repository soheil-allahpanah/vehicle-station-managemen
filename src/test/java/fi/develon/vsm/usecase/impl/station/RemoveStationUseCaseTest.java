package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RemoveStationRequest;
import fi.develon.vsm.domain.core.dto.RemoveStationResponse;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.StationNotBelongToClaimedOwner;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RemoveStationUseCase;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class RemoveStationUseCaseTest {

    private final StationRepository stationRepository = Mockito.mock(StationRepository.class);

    private final CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);

    private RemoveStationUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new RemoveStationUseCaseImpl(companyRepository, stationRepository);
    }


    @Test
    void givenRemoveStationRequestWhenCompanyNotFoundThrowCompanyNotRegisteredException() {
        RemoveStationRequest request = new RemoveStationRequest(new IdentificationNumber("1000"), new StationId(1L));

        doReturn(Optional.empty()).when(companyRepository).findByIdentificationNumber(request.getOwner());
        var response = useCase.remove(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(CompanyNotRegisteredException.class));
    }

    @Test
    void givenRemoveStationRequestWhenStationNotFoundThrowStationNotFoundException() {
        RemoveStationRequest request = new RemoveStationRequest(new IdentificationNumber("1000"), new StationId(1L));
        var company = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());

        doReturn(Optional.of(company)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.empty()).when(stationRepository).findById(request.getRemovingStation());
        var response = useCase.remove(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(StationNotFoundException.class));
    }

    @Test
    void givenRemoveStationRequestWhenStationCompanyIdIsNotEqualToRetriedCompanyThrowStationNotBelongToClaimedOwner() {
        RemoveStationRequest request = new RemoveStationRequest(new IdentificationNumber("1000"), new StationId(1L));
        var company = new Company(new CompanyId(1L)
                , null
                , new IdentificationNumber("1000")
                , new CompanyName("c0"), null
                , LocalDateTime.now()
                , LocalDateTime.now());
        var station = Station.builder()
                .owner(new CompanyId(2L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(new GeoLocation(BigDecimal.valueOf(27.2138), BigDecimal.valueOf(2.4677)))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        doReturn(Optional.of(company)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getRemovingStation());
        var response = useCase.remove(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(StationNotBelongToClaimedOwner.class));
    }


    @Test
    void givenRemoveStationRequestWhenEveryThingIsOkShouldReturnProperResponse() {
        RemoveStationRequest request = new RemoveStationRequest(new IdentificationNumber("1000"), new StationId(1L));
        var company = new Company(new CompanyId(1L)
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

        doReturn(Optional.of(company)).when(companyRepository).findByIdentificationNumber(request.getOwner());
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getRemovingStation());
        doNothing().when(stationRepository).delete(station);
        var response = useCase.remove(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getRemovedStation(), equalTo(request.getRemovingStation()));
    }


}
