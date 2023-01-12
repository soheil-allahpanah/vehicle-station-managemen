package fi.develon.vsm.adapter.in.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameCompanyResDto {

    @ApiModelProperty(value = "Company's new name. after it saved")
    private String name;

    @ApiModelProperty(value = "Company's identification number. after it saved")
    private String identificationNumber;

    @ApiModelProperty(value = "updating time")
    private LocalDateTime updatedAt;
}
