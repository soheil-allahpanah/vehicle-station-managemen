package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCompanySubsidiariesResDto {

    @ApiModelProperty(value = "Company's name")
    private String name;

    @ApiModelProperty(value = "Company's identification number")
    private String identificationNumber;

    @ApiModelProperty(value = "Company's subsidiaries in nested form")
    private List<GetCompanySubsidiariesResDto> subsidiaries;
}
