package fi.develon.vsm.adapter.in.controller.web.station;

import fi.develon.vsm.adapter.in.controller.dto.RelocateStationReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RelocateStationResDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationId;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.station.RelocateStationUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequestMapping("/api/v1/stations")
@RestController
public class RelocateStationController {

    private final RelocateStationUseCase relocateStationUseCase;

    @Autowired
    public RelocateStationController(RelocateStationUseCase relocateStationUseCase) {
        this.relocateStationUseCase = relocateStationUseCase;
    }

    @ApiOperation(value = "Relocate a given station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company Or Station not found"),
            @ApiResponse(code = 409, message = "New location has conflict to other station's location"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("/{station_id}/relocate")
    @ResponseBody
    public RelocateStationResDto relocate(@NotNull @PathVariable(name = "station_id") Long stationId
            , @Valid @RequestBody RelocateStationReqDto request) {

        var res = relocateStationUseCase.relocate(RelocateStationRequest.builder()
                .id(new StationId(stationId))
                .newLocation(new GeoLocation(request.getLatitude(), request.getLongitude()))
                .build()).get();
        return RelocateStationResDto.builder()
                .id(res.getId().value())
                .longitude(res.getNewLocation().longitude())
                .latitude(res.getNewLocation().latitude())
                .build();

    }
}
