package fi.develon.vsm.adapter.in.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCompanyResDto {

    @ApiModelProperty(value = "Company's name after it's saved.")
    private String name;

    @ApiModelProperty(value = "Company's identification number after it's saved.")
    private String identificationNumber;

    @ApiModelProperty(value = "Company's parent name if it set in request")
    private String parentName;

    @ApiModelProperty(value = "Company's parent identification number if it set in request")
    private String parentIdentificationNumber;

    @ApiModelProperty(value = "update date/time. in this request it's equal to creation date/time")
    private LocalDateTime updatedAt;
}
