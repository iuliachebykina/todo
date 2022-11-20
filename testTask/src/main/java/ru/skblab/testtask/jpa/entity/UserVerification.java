package ru.skblab.testtask.jpa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Table(schema = "test_task", name = "user_verification")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
@Where(clause="deleted=false")
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne(mappedBy = "verification")
    User user;

    @Builder.Default
    Boolean isNotificationSending = Boolean.FALSE;

    @Builder.Default
    Boolean isVerificationMessageSending = Boolean.FALSE;

    Boolean isVerified;

    @JsonIgnore
    @Builder.Default
    Boolean deleted = Boolean.FALSE;


}