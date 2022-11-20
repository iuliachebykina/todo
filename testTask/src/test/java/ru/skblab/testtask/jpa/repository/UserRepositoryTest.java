package ru.skblab.testtask.jpa.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skblab.testtask.TestTaskApplication;
import ru.skblab.testtask.dto.NameDto;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.entity.valueType.Name;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestTaskApplication.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final static NameDto name = NameDto.builder()
            .firstName("Юлия")
            .lastName("Чебыкина")
            .patronymic("Владимировна")
            .build();

    private final static String email = "iulia@gmail.ru";
    private final static String login = "iulia";
    private final static String password = "qwerty";


    private final static User savedUser = new User(
            null,
            login,
            email,
            password,
            new Name(name.getFirstName(), name.getLastName(), name.getLastName()),
            new UserVerification(),
            false);

    private final static UserVerification deletedUserVerification = new UserVerification(null, null, true, true, false, true);


    private final static User deletedUser = new User(
            null,
            "some login",
            "someEmail@mail.ru",
            password,
            new Name(name.getFirstName(), name.getLastName(), name.getLastName()),
            deletedUserVerification,
            true);


    @BeforeEach
    public void saveUsers() {
        userRepository.save(savedUser);
        userRepository.save(deletedUser);
    }

    @AfterEach
    public void deleteUsers() {
        userRepository.deleteAll();
    }


    @Test
    public void findByWrongLogin() {
        Optional<User> user = userRepository.findByLogin("wrong");
        assertTrue(user.isEmpty());
    }

    @Test
    public void findByWrongEmail() {
        Optional<User> user = userRepository.findByEmail("wrong");
        assertTrue(user.isEmpty());
    }


    @Test
    public void findByWrongId() {
        Optional<User> user = userRepository.findById(543454L);
        assertTrue(user.isEmpty());
    }


    @Test
    public void findByLogin() {
        Optional<User> user = userRepository.findByLogin(login);
        assertTrue(user.isPresent());
        assertEquals(savedUser.getLogin(), user.get().getLogin());
        assertEquals(savedUser.getEmail(), user.get().getEmail());
        assertEquals(savedUser.getPassword(), user.get().getPassword());
        assertEquals(savedUser.getName(), user.get().getName());

    }

    @Test
    public void findByEmail() {
        Optional<User> user = userRepository.findByEmail(email);
        assertTrue(user.isPresent());
        assertEquals(savedUser.getLogin(), user.get().getLogin());
        assertEquals(savedUser.getEmail(), user.get().getEmail());
        assertEquals(savedUser.getPassword(), user.get().getPassword());
        assertEquals(savedUser.getName(), user.get().getName());
    }

    @Test
    public void findAll() {
        List<User> all = userRepository.findAll();
        assertEquals(1, all.size());
    }


}