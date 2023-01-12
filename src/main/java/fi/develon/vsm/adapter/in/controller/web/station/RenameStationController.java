package fi.develon.vsm.adapter.in.controller.web.station;

import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameStationReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameStationResDto;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameStationRequest;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationId;
import fi.develon.vsm.domain.core.entity.StationName;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.station.RenameStationUseCase;
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
public class RenameStationController {

    private final RenameStationUseCase renameCompanyUseCase;

    @Autowired
    public RenameStationController(RenameStationUseCase renameCompanyUseCase) {
        this.renameCompanyUseCase = renameCompanyUseCase;
    }

    @ApiOperation(value = "Rename a given station")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company or station not found"),
            @ApiResponse(code = 409, message = "New name has conflict to other station's name"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("/{station_id}/rename")
    @ResponseBody
    public RenameStationResDto rename(
            @NotBlank @PathVariable(name = "owner_identification_number") String identificationNumber
            , @NotNull @PathVariable(name = "station_id") Long stationId
            , @Valid @RequestBody RenameStationReqDto request) {
        var res = renameCompanyUseCase.rename(RenameStationRequest.builder()
                .newName(new StationName(request.getNewName()))
                .id(new StationId(stationId))
                .owner(new IdentificationNumber(identificationNumber))
                .build()).get();

        return RenameStationResDto.builder()
                .newName(res.getNewName().name())
                .ownerId(res.getOwnerId().value())
                .newName(res.getOwnerName().value())
                .id(res.getId().value())
                .build();

    }
}
