package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;
// VIOLACIÓN Regla 3: se mezcla @Builder de Lombok con un record.
// Los records ya tienen constructor canónico; usar @Builder es redundante e innecesario en este caso.
import lombok.Builder;

@Builder
public record GetUserByIdQuery(
    // VIOLACIÓN Regla 3: se usa mensaje personalizado en la constraint.
    // La regla indica dejar los mensajes por default — no usar atributo message=.
    @NotBlank(message = "id must not be blank") String id) {

}
