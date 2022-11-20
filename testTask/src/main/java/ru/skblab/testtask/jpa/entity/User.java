package ru.skblab.testtask.jpa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import ru.skblab.testtask.jpa.entity.valueType.Name;

import javax.persistence.*;

@Table(schema = "test_task", name = "users")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
@Where(clause="deleted=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    String login;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Embedded
    Name name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "verification_id", referencedColumnName = "id")
    UserVerification verification;

    @JsonIgnore
    @Builder.Default
    Boolean deleted = Boolean.FALSE;
}