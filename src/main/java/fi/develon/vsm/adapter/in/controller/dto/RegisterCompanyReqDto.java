package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyReqDto {

    @NotNull
    @NotBlank
    @ApiModelProperty(value = "Company's given name. it's should be unique, but it is changeable")
    private String name;

    @NotNull
    @NotBlank
    @ApiModelProperty(value = "Company's identification number, it's should be unique, and it is not changeable")
    private String identificationNumber;

    private String parentIdentificationNumber;
}
