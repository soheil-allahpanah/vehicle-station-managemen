package fi.develon.vsm.adapter.in.controller.mapper;

import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyResDto;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.RegisterCompanyResponse;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;

public class RegisterCompanyControllerMapper implements ControllerMapper<RegisterCompanyReqDto, RegisterCompanyRequest, RegisterCompanyResDto, RegisterCompanyResponse>{

    @Override
    public RegisterCompanyRequest toObj(RegisterCompanyReqDto dto) {
        return RegisterCompanyRequest.builder()
                .parentIdentificationNumber(ObjUtil.checkIfNotNull(dto.getParentIdentificationNumber() , () -> new IdentificationNumber(dto.getParentIdentificationNumber())))
                .identificationNumber(new IdentificationNumber(dto.getIdentificationNumber()))
                .name(new CompanyName(dto.getName()))
                .build();
    }

    @Override
    public RegisterCompanyResDto toDto(RegisterCompanyResponse res) {
        return RegisterCompanyResDto.builder()
                .identificationNumber(res.getIdentificationNumber().value())
                .name(res.getName().value())
                .parentIdentificationNumber(ObjUtil.checkIfNotNull(res.getParentIdentificationNumber(), () -> res.getParentIdentificationNumber().value()))
                .parentName(ObjUtil.checkIfNotNull(res.getParentName(), () -> res.getParentName().value()))
                .updatedAt(res.getUpdatedAt())
                .build();
    }
}
