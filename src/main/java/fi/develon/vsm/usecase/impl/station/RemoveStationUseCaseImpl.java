package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RemoveStationRequest;
import fi.develon.vsm.domain.core.dto.RemoveStationResponse;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.CompanyNotRegisteredException;
import fi.develon.vsm.usecase.exception.StationNotBelongToClaimedOwner;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RemoveStationUseCase;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RemoveStationUseCaseImpl implements RemoveStationUseCase {

    CompanyRepository companyRepository;
    StationRepository stationRepository;

    public RemoveStationUseCaseImpl(CompanyRepository companyRepository, StationRepository stationRepository) {
        this.companyRepository = companyRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<RemoveStationResponse> remove(RemoveStationRequest request) {
        log.info("remove station with id {} from company with identification number: {}", request.getRemovingStation().value()
                , request.getOwner().value());
        Optional<Company> ownerOpt = companyRepository.findByIdentificationNumber(request.getOwner());
        log.debug("company with identification number {} not found", request.getOwner().value());
        if (ownerOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException("Owner company with given identificationNumber not found", 404));
        }
        Optional<Station> stationOpt = stationRepository.findById(request.getRemovingStation());
        log.debug("station with id  {} not found", request.getRemovingStation().value());
        if (stationOpt.isEmpty()) {
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        if (!stationOpt.get().getOwner().equals(ownerOpt.get().getId())) {
            log.debug("station with name {} not belong to company with name {}", stationOpt.get().getName().name(), ownerOpt.get().getName().value());
            return Try.failure(new StationNotBelongToClaimedOwner("specified station not belong to its claimed owner", 409));
        }
        stationRepository.delete(stationOpt.get());
        log.info("station with name {} removed", stationOpt.get().getName());
        return Try.success(RemoveStationResponse.builder()
                .removedStation(stationOpt.get().getStationId())
                .owner(ownerOpt.get().getIdentificationNumber())
                .ownerName(ownerOpt.get().getName())
                .build()
        );
    }
}
