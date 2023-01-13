package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RenameStationRequest;
import fi.develon.vsm.domain.core.dto.RenameStationResponse;
import fi.develon.vsm.domain.core.entity.*;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.DuplicateStationByLocationException;
import fi.develon.vsm.usecase.exception.DuplicateStationByNameException;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RelocateStationUseCase;
import fi.develon.vsm.usecase.station.RenameStationUseCase;
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

public class RenameStationUseCaseTest {

    private final StationRepository stationRepository = Mockito.mock(StationRepository.class);
    private RenameStationUseCase useCase;

    @BeforeEach
    void initUseCase() {
        useCase = new RenameStationUseCaseImpl(stationRepository);
    }



    @Test
    void givenRenameStationRequestWhenStationNotFoundThrowStationNotFoundException() {
        var name = new StationName("st2");
        RenameStationRequest request = new RenameStationRequest(new StationId(1L), name);

        doReturn(Optional.empty()).when(stationRepository).findById(request.getId());
        var response = useCase.rename(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(StationNotFoundException.class));
    }

    @Test
    void givenRenameStationRequestWhenNameIsDuplicatedThrowDuplicateStationByNameException() {
        var name = new StationName("st2");
        RenameStationRequest request = new RenameStationRequest(new StationId(1L), name);
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(new StationName("st1"))
                .stationId(new StationId(1L))
                .location(null)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getId());
        doReturn(Optional.of(station)).when(stationRepository).findByName(name);
        var response = useCase.rename(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(DuplicateStationByNameException.class));
    }

    @Test
    void givenRelocateStationRequestWhenEveryThingIsOkShouldReturnProperResponse() {
        var name = new StationName("st1");
        var newName = new StationName("st2");
        RenameStationRequest request = new RenameStationRequest(new StationId(1L), newName);
        var station = Station.builder()
                .owner(new CompanyId(1L))
                .name(name)
                .stationId(new StationId(1L))
                .location(null)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        doReturn(Optional.of(station)).when(stationRepository).findById(request.getId());
        doReturn(Optional.empty()).when(stationRepository).findByName(newName);
        doReturn(station).when(stationRepository).save(any(Station.class));
        var response = useCase.rename(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getNewName(), equalTo(newName));
    }

}
