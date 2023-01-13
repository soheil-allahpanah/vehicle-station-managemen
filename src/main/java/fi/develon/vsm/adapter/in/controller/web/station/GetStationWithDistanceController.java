package fi.develon.vsm.adapter.in.controller.web.station;

import fi.develon.vsm.adapter.in.controller.dto.GetStationsWithDistanceResDto;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceRequest;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.usecase.station.GetStationsWithDistanceUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/v1/companies")
@RestController
public class GetStationWithDistanceController {

    private final GetStationsWithDistanceUseCase getStationsWithDistanceUseCase;

    @Autowired
    public GetStationWithDistanceController(GetStationsWithDistanceUseCase getStationsWithDistanceUseCase) {
        this.getStationsWithDistanceUseCase = getStationsWithDistanceUseCase;
    }

    @ApiOperation(value = "Given a specific location, this service return all nearby stations from a specific company and its subsidiaries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{identification_number}/nearbyStation")
    @ResponseBody
    public List<GetStationsWithDistanceResDto> subsidiaries(@NotBlank @PathVariable(name = "identification_number") String identificationNumber
            , @Valid @NotNull @RequestParam(name = "lat") String latitude
            , @Valid @NotNull @RequestParam(name = "long") String longitude
            , @RequestParam(name = "radius", required = false) String radius
    ) {
        var res = getStationsWithDistanceUseCase.fetch(GetStationsWithDistanceRequest.builder()
                .location(new GeoLocation(BigDecimal.valueOf(Double.parseDouble(latitude)), BigDecimal.valueOf(Double.parseDouble(longitude))))
                .identificationNumber(new IdentificationNumber(identificationNumber))
                .radius(ObjUtil.checkIfNotNull(radius, () -> Long.parseLong(radius)))
                .build()
        ).get();

        return res.stream().map(r -> GetStationsWithDistanceResDto.builder()
                .stationId(r.getStationId().value())
                .ownerName(r.getCompanyName().value())
                .ownerIdentificationNumber(r.getIdentificationNumber().value())
                .stationName(r.getStationName().name())
                .latitude(r.getLocation().latitude())
                .longitude(r.getLocation().longitude())
                .distance(r.getDistance())
                .distanceInKm(r.getDistanceByKm())
                .build()
        ).toList();
    }
}
