package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.DuplicateCompanyByNameException;
import fi.develon.vsm.usecase.exception.DuplicateStationByLocationException;
import fi.develon.vsm.usecase.exception.DuplicateStationByNameException;
import fi.develon.vsm.usecase.station.AddStationToCompanyUseCase;
import io.vavr.control.Try;

import java.time.LocalDateTime;
import java.util.Optional;

public class AddStationToCompanyUseCaseImpl implements AddStationToCompanyUseCase {
    CompanyRepository companyRepository;
    StationRepository stationRepository;

    public AddStationToCompanyUseCaseImpl(CompanyRepository companyRepository, StationRepository stationRepository) {
        this.companyRepository = companyRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<AddStationToCompanyResponse> addStation(AddStationToCompanyRequest request) {
        Optional<Company> ownerOpt = companyRepository.findByIdentificationNumber(request.getOwner());
        if (ownerOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException("Owner company with given identificationNumber not found", 404));
        }
        Optional<Station> stationByNameOpt = stationRepository.findByName(request.getName());
        if (stationByNameOpt.isPresent()) {
            return Try.failure(new DuplicateStationByNameException("Owner company with given identificationNumber not found", 409));
        }
        Optional<Station> stationByLocationOpt = stationRepository.findByLocation(request.getLocation());
        if (stationByLocationOpt.isPresent()) {
            return Try.failure(new DuplicateStationByLocationException("Owner company with given identificationNumber not found", 409));
        }

        Station station = Station.builder()
                .owner(ownerOpt.get().getId())
                .name(request.getName())
                .location(request.getLocation())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        var savedStation = stationRepository.save(station);
        return Try.success(AddStationToCompanyResponse.builder()
                .ownerId(ownerOpt.get().getIdentificationNumber())
                .ownerName(ownerOpt.get().getName())
                .location(savedStation.getLocation())
                .name(savedStation.getName())
                .updatedAt(savedStation.getUpdatedAt())
                .build());
    }
}
