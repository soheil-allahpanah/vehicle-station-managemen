package fi.develon.vsm.adapter.in.controller.mapper;

import fi.develon.vsm.adapter.in.controller.dto.GetCompanySubsidiariesResDto;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesResponse;

public class GetCompanySubsidiariesControllerMapper implements ControllerMapper<Void, Void, GetCompanySubsidiariesResDto, GetCompanySubsidiariesResponse> {
    @Override
    public Void toObj(Void unused) {
        return null;
    }

    @Override
    public GetCompanySubsidiariesResDto toDto(GetCompanySubsidiariesResponse res) {
        return GetCompanySubsidiariesResDto.builder()
                .identificationNumber(ObjUtil.checkIfNotNull(res.getIdentificationNumber(), () -> res.getIdentificationNumber().value()))
                .name(ObjUtil.checkIfNotNull(res.getName(), () -> res.getName().value()))
                .subsidiaries(ObjUtil.checkIfNotNull(res.getSubsidiaries(), () -> res.getSubsidiaries().stream().map(this::toDto).toList()))
                .build();
    }
}
