package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeParentOfCompanyResDto {

    @ApiModelProperty(value = "Company's name")
    private String name;

    @ApiModelProperty(value = "Company's identification number")
    private String identificationNumber;

    @ApiModelProperty(value = "Company's new parent name")
    private String parentName;

    @ApiModelProperty(value = "Company's new parent identification number")
    private String parentIdentificationNumber;

    @ApiModelProperty(value = "update date/time in iso format")
    private LocalDateTime updatedAt;

}
