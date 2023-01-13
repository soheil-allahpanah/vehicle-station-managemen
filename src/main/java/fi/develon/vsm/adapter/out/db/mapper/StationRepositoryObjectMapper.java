package fi.develon.vsm.adapter.out.db.mapper;

import fi.develon.vsm.adapter.out.db.entity.StationEntity;
import fi.develon.vsm.common.ObjUtil;
import fi.develon.vsm.domain.core.entity.*;

public class StationRepositoryObjectMapper implements RepositoryObjectMapper<StationEntity, Station>  {
    @Override
    public Station toDomain(StationEntity entity) {
        return Station.builder()
                .stationId(new StationId(entity.getId()))
                .owner(new CompanyId(entity.getOwner()))
                .name(new StationName(entity.getName()))
                .location(new GeoLocation(entity.getLatitude(), entity.getLongitude()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public StationEntity toEntity(Station obj) {
        StationEntity station = new StationEntity();
        station.setId(ObjUtil.checkIfNotNull(obj.getStationId(), () -> obj.getStationId().value()));
        station.setName(ObjUtil.checkIfNotNull(obj.getName(), () -> obj.getName().name()));
        station.setOwner(ObjUtil.checkIfNotNull(obj.getOwner(), () -> obj.getOwner().value()));
        station.setLatitude(ObjUtil.checkIfNotNull(obj.getLocation(), () -> obj.getLocation().latitude()));
        station.setLongitude(ObjUtil.checkIfNotNull(obj.getLocation(), () -> obj.getLocation().longitude()));
        station.setUpdatedAt(obj.getUpdatedAt());
        station.setCreatedAt(obj.getCreatedAt());
        return station;
    }

}
