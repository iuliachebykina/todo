package ru.skblab.testtask.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.skblab.testtask.dto.UserDto;
import ru.skblab.testtask.exeption.EmailExistException;
import ru.skblab.testtask.exeption.LoginExistException;
import ru.skblab.testtask.service.RegistrationService;

import java.util.concurrent.TimeoutException;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterController {

    final RegistrationService registrationService;
    @Value("${app.register.message.email-existing}")
    String emailExistingMessage;
    @Value("${app.register.message.login-existing}")
    String loginExistingMessage;
    @Value("${app.register.message.unsuccessful}")
    String unsuccessfulMessage;


    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/success")
    public String successRegistration() {
        return "success";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {

        try {
            registrationService.registerUser(userDto);
        } catch (LoginExistException e) {
            result.rejectValue("login", null,
                    loginExistingMessage);
        } catch (EmailExistException e) {
            result.rejectValue("email", null,
                    loginExistingMessage);
        } catch (TimeoutException e) {
            result.rejectValue("password", "400",
                    "smth wring");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        return "redirect:/success";
    }

}
