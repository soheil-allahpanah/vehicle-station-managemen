package fi.develon.vsm.adapter.in.controller.web.company;

import fi.develon.vsm.adapter.in.controller.dto.ChangeParentOfCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.ChangeParentOfCompanyResDto;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.usecase.company.ChangeParentOfCompanyUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping("/api/v1/companies")
@RestController
public class ChangeParentCompanyController {

    private final ChangeParentOfCompanyUseCase changeParentOfCompanyUseCase;

    @Autowired
    public ChangeParentCompanyController(ChangeParentOfCompanyUseCase changeParentOfCompanyUseCase) {
        this.changeParentOfCompanyUseCase = changeParentOfCompanyUseCase;
    }

    @ApiOperation(value = "Change parent of  a given company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Company not found"),
            @ApiResponse(code = 409, message = "New name has conflict to other company's name"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("/{identification_number}/changeParent")
    @ResponseBody
    public ChangeParentOfCompanyResDto rename(@NotBlank @PathVariable(name = "identification_number") String identificationNumber
            , @Valid @RequestBody ChangeParentOfCompanyReqDto request) {

        var response = changeParentOfCompanyUseCase.changeParent(
                new ChangeParentOfCompanyRequest(new IdentificationNumber(identificationNumber)
                , new IdentificationNumber(request.getNewParentIdentificationNumber()))).get();
        return new ChangeParentOfCompanyResDto(
                response.getName().value()
                , response.getIdentificationNumber().value()
                , response.getParentName().value()
                , response.getParentIdentificationNumber().value()
                , response.getUpdatedAt()
        );
    }
}
