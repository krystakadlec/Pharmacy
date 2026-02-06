package pharmacy.controller.converter;

public interface DTOConverter <DTO, Entity>{
    DTO toDTO(Entity e);

    Entity toEntity(DTO d);
}

