package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyResponse;
import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RelocateStationResponse;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateStationByLocationException;
import fi.develon.vsm.usecase.exception.DuplicateStationByNameException;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.AddStationToCompanyUseCase;
import fi.develon.vsm.usecase.station.RelocateStationUseCase;
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
import static org.mockito.Mockito.doReturn;

public class RelocateStationUseCaseTest {

    private final StationRepository stationRepository = Mockito.mock(StationRepository.class);
    private RelocateStationUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new RelocateStationUseCaseImpl(stationRepository);
    }

    @Test
    void givenRelocateStationRequestWhenStationNotFoundThrowStationNotFoundException() {
        var location = new GeoLocation(BigDecimal.valueOf(27.2137), BigDecimal.valueOf(2.4671));
        RelocateStationRequest request = new RelocateStationRequest(new StationId(1L), location);

        doReturn(Optional.empty()).when(stationRepository).findById(request.getId());
        var response = useCase.relocate(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(StationNotFoundException.class));
    }

    @Test
    void givenRelocateStationRequestWhenLocationIsDuplicatedThrowDuplicateStationByLocationException() {
        var location = new GeoLocation(BigDecimal.valueOf(27.2137), BigDecimal.valueOf(2.4671));
        RelocateStationRequest request = new RelocateStationRequest(new StationId(1L), location);
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(location)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getId());
        doReturn(Optional.of(station)).when(stationRepository).findByLocation(location);
        var response = useCase.relocate(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateStationByLocationException.class));
    }

    @Test
    void givenRelocateStationRequestWhenEveryThingIsOkShouldReturnProperResponse() {
        var location = new GeoLocation(BigDecimal.valueOf(27.2137), BigDecimal.valueOf(2.4671));
        var newLocation = new GeoLocation(BigDecimal.valueOf(27.2167), BigDecimal.valueOf(2.4571));
        RelocateStationRequest request = new RelocateStationRequest(new StationId(1L), newLocation);
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(location)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getId());
        doReturn(Optional.empty()).when(stationRepository).findByLocation(newLocation);
        doReturn(station).when(stationRepository).save(any(Station.class));
        var response = useCase.relocate(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getNewLocation(), equalTo(newLocation));
    }

}
