package fi.develon.vsm.adapter.in.controller.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameCompanyReqDto {
    @NotNull
    @NotBlank
    @ApiModelProperty(value = "Company's new name. and it must not registered by others")
    private String newName;
}
