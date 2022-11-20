package ru.skblab.testtask.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVerifiedRequestMessage extends Message {
    String email;
    NameDto name;
}
