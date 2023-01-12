package fi.develon.vsm.adapter.in.controller.web;

import fi.develon.vsm.adapter.in.controller.dto.GetCompanySubsidiariesResDto;
import fi.develon.vsm.adapter.in.controller.mapper.GetCompanySubsidiariesControllerMapper;
import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesRequest;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;
import fi.develon.vsm.usecase.GetCompanySubsidiariesUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RequestMapping("/api/v1/companies")
@RestController
public class GetCompanySubsidiariesController {

    private final GetCompanySubsidiariesUseCase getCompanySubsidiariesUseCase;
    private final GetCompanySubsidiariesControllerMapper mapper = new GetCompanySubsidiariesControllerMapper();

    @Autowired
    public GetCompanySubsidiariesController(GetCompanySubsidiariesUseCase getCompanySubsidiariesUseCase) {
        this.getCompanySubsidiariesUseCase = getCompanySubsidiariesUseCase;
    }

    @ApiOperation(value = "Register new company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Parent company not found"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/{identification_number}/subsidiaries")
    @ResponseBody
    public GetCompanySubsidiariesResDto subsidiaries(@NotBlank @PathVariable(name = "identification_number") String identificationNumber) {
        return mapper.toDto(getCompanySubsidiariesUseCase.getSubsidiaries(new GetCompanySubsidiariesRequest(new IdentificationNumber(identificationNumber))).get());
    }
}
