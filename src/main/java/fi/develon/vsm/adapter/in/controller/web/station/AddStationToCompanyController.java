package fi.develon.vsm.adapter.in.controller.web.station;

import fi.develon.vsm.adapter.in.controller.dto.AddStationToCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.AddStationToCompanyResDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.GeoLocation;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.domain.core.entity.StationName;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import fi.develon.vsm.usecase.station.AddStationToCompanyUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/api/v1/companies")
@RestController
public class AddStationToCompanyController {

    private final AddStationToCompanyUseCase addStationToCompanyUseCase;

    @Autowired
    public AddStationToCompanyController(AddStationToCompanyUseCase addStationToCompanyUseCase) {
        this.addStationToCompanyUseCase = addStationToCompanyUseCase;
    }

    @ApiOperation(value = "Add station to given company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/{owner_identification_number}/addStation")
    @ResponseBody
    public AddStationToCompanyResDto addStation(@NotBlank @PathVariable(name = "owner_identification_number") String identificationNumber
            , @Valid @RequestBody AddStationToCompanyReqDto request) {

        var res = addStationToCompanyUseCase.addStation(AddStationToCompanyRequest.builder()
                .owner(new IdentificationNumber(identificationNumber))
                .name(new StationName(request.getName()))
                .location(new GeoLocation(request.getLatitude(), request.getLongitude()))
                .build()).get();

        return AddStationToCompanyResDto.builder()
                .ownerId(res.getOwnerId().value())
                .ownerName(res.getOwnerName().value())
                .stationId(res.getStationId().value())
                .stationName(res.getName().name())
                .longitude(res.getLocation().longitude())
                .latitude(res.getLocation().latitude())
                .build();
    }
}
