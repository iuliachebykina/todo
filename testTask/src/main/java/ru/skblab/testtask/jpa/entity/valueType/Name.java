package ru.skblab.testtask.jpa.entity.valueType;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Name {
    @Column(length = 50, nullable = false)
    String firstName;
    @Column(length = 50, nullable = false)
    String lastName;
    @Column(length = 50)
    String patronymic;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return String.format("%s %s %s", lastName, firstName, patronymic);
    }
}
