package fi.develon.vsm.adapter.in.controller.web.company;

import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyResDto;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.usecase.company.RenameCompanyUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/api/v1/companies")
@RestController
public class RenameCompanyController {

    private final RenameCompanyUseCase renameCompanyUseCase;

    @Autowired
    public RenameCompanyController(RenameCompanyUseCase renameCompanyUseCase) {
        this.renameCompanyUseCase = renameCompanyUseCase;
    }

    @ApiOperation(value = "Rename a given company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 409, message = "New name has conflict to other company's name"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("/{identification_number}/rename")
    @ResponseBody
    public RenameCompanyResDto rename(@NotBlank @PathVariable(name = "identification_number") String identificationNumber
            ,@Valid @RequestBody RenameCompanyReqDto request) {

        var response = renameCompanyUseCase.rename(new RenameCompanyRequest(new IdentificationNumber(identificationNumber)
                , new CompanyName(request.getNewName()))).get();
        return new RenameCompanyResDto(response.getName().value(), response.getIdentificationNumber().value(), response.getUpdatedAt());
    }
}
