package fi.develon.vsm.adapter.in.controller.web;

import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyResDto;
import fi.develon.vsm.adapter.in.controller.mapper.RegisterCompanyControllerMapper;
import fi.develon.vsm.usecase.RegisterCompanyUseCase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/companies")
@RestController
public class RegisterCompanyController {

    private final RegisterCompanyUseCase registerCompanyUseCase;
    private final RegisterCompanyControllerMapper mapper = new RegisterCompanyControllerMapper();

    @Autowired
    public RegisterCompanyController(RegisterCompanyUseCase registerCompanyUseCase) {
        this.registerCompanyUseCase = registerCompanyUseCase;
    }

    @ApiOperation(value = "Register new company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully perform the operation"),
            @ApiResponse(code = 400, message = "The Request property was not provide correctly."),
            @ApiResponse(code = 404, message = "Parent company not found"),
            @ApiResponse(code = 409, message = "Company by (Name or identificationNumber) already exist"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/register")
    @ResponseBody
    public RegisterCompanyResDto register(@Valid @RequestBody RegisterCompanyReqDto request) {
        return mapper.toDto(registerCompanyUseCase.register(mapper.toObj(request)).get());
    }
}
