package ru.skblab.testtask.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skblab.testtask.dto.UserDto;
import ru.skblab.testtask.exeption.EmailExistException;
import ru.skblab.testtask.exeption.LoginExistException;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.entity.valueType.Name;
import ru.skblab.testtask.jpa.repository.UserRepository;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class RegistrationServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RegistrationServiceImpl registrationService;
    private final static String email = "iulia@gmail.com";
    private final static String login = "iulia";
    private final static String password = "qwerty";




    private final static UserVerification userVerification = new UserVerification();


    private final static UserDto userDto = UserDto.builder()
            .login(login)
            .password(password)
            .email(email)
            .lastName("Чебыкина")
            .firstName("Юлия")
            .patronymic("Владимировна")
            .build();

    private final static User userWithSameLogin = new User(1L,
            login,
            "smth else",
            password,
            new Name(userDto.getFirstName(), userDto.getLastName(), userDto.getLastName()),
            userVerification,
            false);

    private final static User userWithSameEmail = new User(1L,
            "smth else",
            email,
            password,
            new Name(userDto.getFirstName(), userDto.getLastName(), userDto.getLastName()),
            userVerification,
            false);

    @Test
    public void successRegisterUserTest() throws EmailExistException, LoginExistException {
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(userDto.getPassword() + "salt))");
        registrationService.registerUser(userDto);
    }

    @Test
    public void unsuccessfulRegisterUserWithEmailExistingTest() {
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(userDto.getPassword() + "salt))");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userWithSameEmail));
        assertThrows(EmailExistException.class, () -> registrationService.registerUser(userDto));
    }

    @Test
    public void unsuccessfulRegisterUserWithLoginExistingTest() {
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(userDto.getPassword() + "salt))");
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(userWithSameLogin));
        assertThrows(LoginExistException.class, () -> registrationService.registerUser(userDto));
    }

}