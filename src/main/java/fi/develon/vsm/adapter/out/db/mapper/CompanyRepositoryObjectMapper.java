package fi.develon.vsm.adapter.out.db.mapper;

import fi.develon.vsm.adapter.out.db.entity.CompanyEntity;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.entity.Company;
import fi.develon.vsm.domain.core.entity.CompanyId;
import fi.develon.vsm.domain.core.entity.CompanyName;
import fi.develon.vsm.domain.core.entity.IdentificationNumber;

public class CompanyRepositoryObjectMapper implements RepositoryObjectMapper<CompanyEntity, Company> {


    @Override
    public Company toDomain(CompanyEntity entity) {
        return Company.builder()
                .id(ObjUtil.checkIfNotNull(entity.getId(), () -> new CompanyId(entity.getId())))
                .name(ObjUtil.checkIfNotNull(entity.getName(), () -> new CompanyName(entity.getName())))
                .identificationNumber(ObjUtil.checkIfNotNull(entity.getIdentificationNumber(), () -> new IdentificationNumber(entity.getIdentificationNumber())))
                .parent(ObjUtil.checkIfNotNull(entity.getParent(), () -> new CompanyId(entity.getParent())))
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    public CompanyEntity toEntity(Company obj) {
        var entity = new CompanyEntity();
        entity.setId(ObjUtil.checkIfNotNull(obj.getId(), () -> obj.getId().value()));
        entity.setName(ObjUtil.checkIfNotNull(obj.getName(), () -> obj.getName().value()));
        entity.setIdentificationNumber(ObjUtil.checkIfNotNull(obj.getIdentificationNumber(), () -> obj.getIdentificationNumber().value()));
        entity.setParent(ObjUtil.checkIfNotNull(obj.getParent(), () -> obj.getParent().value()));
        entity.setUpdatedAt(obj.getUpdatedAt());
        entity.setCreatedAt(obj.getCreatedAt());
        return entity;
    }
}
