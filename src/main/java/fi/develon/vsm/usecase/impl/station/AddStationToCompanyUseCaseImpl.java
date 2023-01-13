package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateStationByLocationException;
import fi.develon.vsm.usecase.exception.DuplicateStationByNameException;
import fi.develon.vsm.usecase.station.AddStationToCompanyUseCase;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class AddStationToCompanyUseCaseImpl implements AddStationToCompanyUseCase {
    CompanyRepository companyRepository;
    StationRepository stationRepository;

    public AddStationToCompanyUseCaseImpl(CompanyRepository companyRepository, StationRepository stationRepository) {
        this.companyRepository = companyRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<AddStationToCompanyResponse> addStation(AddStationToCompanyRequest request) {
        log.info("add station {} to company {}", request.getName().name(), request.getOwner().value());
        Optional<Company> ownerOpt = companyRepository.findByIdentificationNumber(request.getOwner());
        if (ownerOpt.isEmpty()) {
            log.debug("owner company with identification number : {} not found", request.getOwner().value());
            return Try.failure(new CompanyNotRegisteredException("Owner company with given identificationNumber not found", 404));
        }
        Optional<Station> stationByNameOpt = stationRepository.findByName(request.getName());
        if (stationByNameOpt.isPresent()) {
            log.debug("station with id {} with same name {} exist", stationByNameOpt.get().getStationId().value(), request.getName().name());
            return Try.failure(new DuplicateStationByNameException("station with same name exist", 409));
        }
        Optional<Station> stationByLocationOpt = stationRepository.findByLocation(request.getLocation());
        if (stationByLocationOpt.isPresent()) {
            log.debug("station with id {} with same location {} exist", stationByLocationOpt.get().getStationId().value(), request.getLocation());
            return Try.failure(new DuplicateStationByLocationException("station with same location exist", 409));
        }

        Station station = Station.builder()
                .owner(ownerOpt.get().getId())
                .name(request.getName())
                .location(request.getLocation())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        var savedStation = stationRepository.save(station);
        log.debug("station with id {} saved successfully", savedStation.getStationId().value());
        return Try.success(AddStationToCompanyResponse.builder()
                .owner(ownerOpt.get().getIdentificationNumber())
                .ownerName(ownerOpt.get().getName())
                .location(savedStation.getLocation())
                .name(savedStation.getName())
                .stationId(savedStation.getStationId())
                .updatedAt(savedStation.getUpdatedAt())
                .build());
    }
}
