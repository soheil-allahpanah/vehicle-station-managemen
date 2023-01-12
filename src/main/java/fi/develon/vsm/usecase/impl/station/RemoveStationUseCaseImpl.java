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

import java.util.Optional;

public class RemoveStationUseCaseImpl implements RemoveStationUseCase {

    CompanyRepository companyRepository;
    StationRepository stationRepository;

    public RemoveStationUseCaseImpl(CompanyRepository companyRepository, StationRepository stationRepository) {
        this.companyRepository = companyRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<RemoveStationResponse> remove(RemoveStationRequest request) {
        Optional<Company> ownerOpt = companyRepository.findByIdentificationNumber(request.getOwner());
        if (ownerOpt.isEmpty()) {
            return Try.failure(new CompanyNotRegisteredException("Owner company with given identificationNumber not found", 404));
        }
        Optional<Station> stationOpt = stationRepository.findById(request.getRemovingStation());
        if (stationOpt.isEmpty()) {
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        if (!stationOpt.get().getOwner().equals(ownerOpt.get().getId())) {
            return Try.failure(new StationNotBelongToClaimedOwner("specified station not belong to claimed owner", 409));
        }
        stationRepository.delete(stationOpt.get());

        return Try.success(RemoveStationResponse.builder()
                .removedStation(stationOpt.get().getStationId())
                .ownerId(ownerOpt.get().getIdentificationNumber())
                .ownerName(ownerOpt.get().getName())
                .build()
        );
    }
}
