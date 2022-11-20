package ru.skblab.testtask.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skblab.testtask.TestTaskApplication;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.entity.valueType.Name;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestTaskApplication.class)
class UserVerificationRepositoryTest {

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserRepository userRepository;


    private final static UserVerification userVerificationWithSentNotification
            = new UserVerification(164756L,
            null,
            true,
            true,
            true,
            false);

    private final static UserVerification userVerificationWithUnSentNotification1
            = new UserVerification(265L,
            null,
            false,
            true,
            true,
            false);
    private final static UserVerification userVerificationWithUnSentNotification2
            = new UserVerification(3766L,
            null,
            false,
            true,
            true,
            false);

    private final static UserVerification userVerificationWithUnSentVerificationMessage1
            = new UserVerification(47658L,
            null,
            false,
            false,
            null,
            false);

    private final static UserVerification userVerificationWithUnSentVerificationMessage2
            = new UserVerification(58678L,
            null,
            false,
            false,
            null,
            false);

    private final static UserVerification deletedUserVerification
            = new UserVerification(null,
            null,
            false,
            false,
            null,
            true);


    private final static User savedUser1 = new User(
            8775861L,
            "login1",
            "email1",
            "password",
            new Name("Iulia", "Chebykina"),
            userVerificationWithSentNotification,
            false);
    private final static User savedUser2 = new User(
            876572L,
            "login2",
            "email2",
            "password",
            new Name("Iulia", "Chebykina"),
            userVerificationWithUnSentNotification1,
            false);
    private final static User savedUser3 = new User(
            875677L,
            "login3",
            "email3",
            "password",
            new Name("Iulia", "Chebykina"),
            userVerificationWithUnSentNotification2,
            false);
    private final static User savedUser4 = new User(
            83574L,
            "login4",
            "email4",
            "password",
            new Name("Iulia", "Chebykina"),
            userVerificationWithUnSentVerificationMessage1,
            false);
    private final static User savedUser5 = new User(
            86475L,
            "login5",
            "email5",
            "password",
            new Name("Iulia", "Chebykina"),
            userVerificationWithUnSentVerificationMessage2,
            false);

    private final static User savedUser6 = new User(
            null,
            "login6",
            "email6",
            "password",
            new Name("Iulia", "Chebykina"),
            deletedUserVerification,
            true);


    @BeforeEach
    public void saveUserVerification() {
        userRepository.deleteAll();
        userRepository.save(savedUser1);
        userRepository.save(savedUser2);
        userRepository.save(savedUser3);
        userRepository.save(savedUser4);
        userRepository.save(savedUser5);
    }

    @BeforeEach
    public void deleteUserVerification() {
        userRepository.deleteAll();
    }




    @Test
    public void findAllTest() {
        List<UserVerification> all = userVerificationRepository.findAll();
        assertEquals(5, all.size());
    }

    @Test
    public void findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNullTest() {
        List<UserVerification> all = userVerificationRepository.findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull();
        assertEquals(2, all.size());
        assertTrue(!all.get(0).getIsNotificationSending() && all.get(0).getIsVerified() != null);
        assertTrue(!all.get(1).getIsNotificationSending() && all.get(1).getIsVerified() != null);
    }

    @Test
    public void findAllByIsVerificationMessageSendingIsFalseTest() {
        List<UserVerification> all = userVerificationRepository.findAllByIsVerificationMessageSendingIsFalse();
        assertEquals(2, all.size());
        assertTrue(!all.get(0).getIsVerificationMessageSending() && all.get(0).getIsVerified() == null);
        assertTrue(!all.get(1).getIsVerificationMessageSending() && all.get(1).getIsVerified() == null);
    }

}