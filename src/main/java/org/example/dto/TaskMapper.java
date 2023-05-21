package org.example.dto;

import org.example.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDTO toTaskDto(Task teg);

    Task mapToEntity(TaskDTO tegDto);
}
