package fi.develon.vsm.adapter.in.controller.web;

import fi.develon.vsm.adapter.in.controller.dto.CompanyInfoResDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.adapter.in.controller.dto.UnregisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.UnregisterCompanyResDto;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.usecase.UnregisterCompanyUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/api/v1/companies")
@RestController
public class UnregisterCompanyController {

    private final UnregisterCompanyUseCase unregisterCompanyUseCase;

    @Autowired
    public UnregisterCompanyController(UnregisterCompanyUseCase unregisterCompanyUseCase) {
        this.unregisterCompanyUseCase = unregisterCompanyUseCase;
    }

    @ApiOperation(value = "unregister a given company and if it asked unregister its whole subsidiary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("/{identification_number}/unregister")
    @ResponseBody
    public UnregisterCompanyResDto unregister(@NotBlank @PathVariable(name = "identification_number") String identificationNumber
            , @Valid @RequestBody UnregisterCompanyReqDto request) {

        var response = unregisterCompanyUseCase.unregister(new UnregisterCompanyRequest(new IdentificationNumber(identificationNumber)
                , request.getUnregisterItsSubsidiary())).get();

        return UnregisterCompanyResDto.builder()
                .identificationNumber(response.getIdentificationNumber().value())
                .unregisteredSubsidiaries(ObjUtil.checkIfNotNull(response.getUnregisteredSubsidiaries() , () -> response.getUnregisteredSubsidiaries().stream()
                        .map( c -> new CompanyInfoResDto(c.getIdentificationNumber().value(), c.getName().value()))
                        .toList()
                )).build();
    }
}
