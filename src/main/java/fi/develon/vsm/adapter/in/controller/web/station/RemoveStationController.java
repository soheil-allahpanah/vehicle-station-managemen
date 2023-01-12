package fi.develon.vsm.adapter.in.controller.web.station;

import fi.develon.vsm.adapter.in.controller.dto.RelocateStationResDto;
import fi.develon.vsm.adapter.in.controller.dto.RemoveStationResDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RemoveStationRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationId;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.station.RemoveStationUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequestMapping("/api/v1/companies")
@RestController
public class RemoveStationController {

    private final RemoveStationUseCase removeStationUseCase;

    @Autowired
    public RemoveStationController(RemoveStationUseCase removeStationUseCase) {
        this.removeStationUseCase = removeStationUseCase;
    }

    @ApiOperation(value = "Remove a given station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company Or Station not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{owner_identification_number}/stations/{station_id}/remove")
    @ResponseBody
    public RemoveStationResDto remove(@NotBlank @PathVariable(name = "owner_identification_number") String identificationNumber
            , @NotNull @PathVariable(name = "station_id") Long stationId) {

        var res = removeStationUseCase.remove(RemoveStationRequest.builder()
                .owner(new IdentificationNumber(identificationNumber))
                .removingStation(new StationId(stationId))
                .build()).get();
        return RemoveStationResDto.builder()
                .removedStation(res.getRemovedStation().value())
                .ownerId(res.getOwnerId().value())
                .ownerName(res.getOwnerName().value())
                .build();

    }
}
