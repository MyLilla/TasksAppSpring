package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Status;

@Getter
@Setter
public class TaskDTO {

    private String description;
    private Status status;
}
