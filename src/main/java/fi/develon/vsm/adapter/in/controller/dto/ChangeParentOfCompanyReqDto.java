package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeParentOfCompanyReqDto {

    @ApiModelProperty(value = "Company's new parent identification number")
    private String newParentIdentificationNumber;
}
