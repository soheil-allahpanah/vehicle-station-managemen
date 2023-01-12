package fi.develon.vsm.adapter.in.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnregisterCompanyReqDto {

    @NotNull
    @ApiModelProperty(value = "for given identification number, it shows is it necessary to unregistered given company's subsidiaries")
    private Boolean unregisterItsSubsidiary;
}
