package fi.develon.vsm.adapter.out.db.mapper;

public interface RepositoryObjectMapper <Entity, DomainObject>{

    DomainObject toDomain(Entity entity);

    Entity toEntity(DomainObject obj);
}
