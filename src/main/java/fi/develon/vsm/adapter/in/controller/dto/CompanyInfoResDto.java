package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Company's basic info")
public class CompanyInfoResDto {

    @ApiModelProperty(value = "Company's identification number")
    private String identificationNumber;

    @ApiModelProperty(value = "Company's name")
    private String name;

}
