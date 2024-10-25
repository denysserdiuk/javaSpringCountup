package ua.denysserdiuk.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.UserCreationService;
import ua.denysserdiuk.utils.EmailService;

import java.io.IOException;
import java.util.Random;

@Controller
public class UserWebController {

    private final UserCreationService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserWebController(UserCreationService userService, UserRepository userRepository, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping("/CreateUser")
    public String createUser(Users users, @RequestParam("repeatPassword") String repeatPassword, Model model, HttpSession session) throws IOException {
        if (!users.getPassword().equals(repeatPassword)) {
            model.addAttribute("passwordError", "Passwords do not match");
            model.addAttribute("users", users); // Retain user input
            return "register";
        }

        Users user = userRepository.findByUsername(users.getUsername());
        if (user != null){
            model.addAttribute("usernameError", "Username is not unique");
            return "register";
        }
        int verificationCode = new Random().nextInt(900000) + 100000;
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("userToRegister", users);

        emailService.sendVerificationEmail(users.getEmail(), verificationCode);

        model.addAttribute("verificationPending",true);

        return "register";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam("verification") int verificationCodeInput, HttpSession session, Model model) {
        Integer verificationCode = (Integer) session.getAttribute("verificationCode");
        Users users = (Users) session.getAttribute("userToRegister");

        if (verificationCode != null && verificationCode.equals(verificationCodeInput)) {
                userService.createUser(users);
                session.removeAttribute("verificationCode");
                session.removeAttribute("userToRegister");
                return "redirect:/login";

        } else {
            model.addAttribute("verificationError", "Invalid verification code");
            model.addAttribute("verificationPending", true);
            return "register";
        }
    }

}
