package fi.develon.vsm.adapter.in.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnregisterCompanyResDto {

    @ApiModelProperty(value = "identification number which belongs to unregistered company")
    private String identificationNumber;

    @ApiModelProperty(value = "subsidiaries that unregistered base on the request")
    private List<CompanyInfoResDto> unregisteredSubsidiaries;

}
