package ua.denysserdiuk.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.model.VerificationToken;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.UserCreationService;
import ua.denysserdiuk.utils.EmailService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class UserRegistrationWebController {

    private final UserCreationService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserRegistrationWebController(UserCreationService userService, UserRepository userRepository, EmailService emailService) {
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
        if (user != null) {
            model.addAttribute("usernameError", "Username is not unique");
            return "register";
        }

        Users user1 = userRepository.findByEmail(users.getEmail());
        if (user1 != null) {
            model.addAttribute("emailError", "Email is not unique");
            return "register";
        }

        int verificationCode = new Random().nextInt(900000) + 100000;
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        VerificationToken token = new VerificationToken(users, verificationCode, expiryDate);
        session.setAttribute("verificationToken", token);

        emailService.sendVerificationEmail(users.getEmail(), verificationCode);

        model.addAttribute("verificationPending", true);

        return "register";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam("verification") int verificationCodeInput, HttpSession session, Model model) {
        VerificationToken token = (VerificationToken) session.getAttribute("verificationToken");

        if (token != null) {
            if (LocalDateTime.now().isAfter(token.getExpiryDate())) {
                model.addAttribute("verificationError", "Verification code has expired. Please request a new one.");
                model.addAttribute("verificationPending", true);
                return "register";
            }

            if (token.getCode() == verificationCodeInput) {
                try {
                    userService.createUser(token.getUser());
                    session.removeAttribute("verificationToken");
                    return "redirect:/login";
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("registrationError", "An error occurred during registration.");
                    return "register";
                }
            } else {
                model.addAttribute("verificationError", "Invalid verification code");
                model.addAttribute("verificationPending", true);
                return "register";
            }
        } else {
            model.addAttribute("verificationError", "No verification code found. Please register again.");
            return "register";
        }
    }

    @PostMapping("/resendVerificationCode")
    @ResponseBody
    public ResponseEntity<String> resendVerificationCode(HttpSession session) throws IOException {
        VerificationToken token = (VerificationToken) session.getAttribute("verificationToken");
        System.out.println("Second code sent");

        if (token != null) {
            int newCode = new Random().nextInt(900000) + 100000;
            token.setCode(newCode);
            token.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // Reset expiry
            session.setAttribute("verificationToken", token);

            emailService.sendVerificationEmail(token.getUser().getEmail(), newCode);
            return ResponseEntity.ok("Verification code resent");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No registration in progress");
        }
    }


}
