package ru.skblab.testtask.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterResponse {
    Boolean isSuccessfully;
    String message;

}
