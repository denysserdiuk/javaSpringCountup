package ua.denysserdiuk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.denysserdiuk.model.Shares;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.SharesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.util.List;

@Controller
public class SharesWebController {
    private final UserRepository userRepository;
    private final SharesService sharesService;

    public SharesWebController(UserRepository userRepository, SharesService sharesService) {
        this.userRepository = userRepository;
        this.sharesService = sharesService;
    }

    @PostMapping("/addShare")
    public String addShare(Shares share, Model model){
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        share.setUser(user);

        sharesService.addShare(share);
        model.addAttribute("message", "New share saved");

        return "shares";
    }
    @GetMapping("/user-shares")
    public ResponseEntity<List<Shares>> getUserShares() {
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);

        List<Shares> userShares = sharesService.findByUserId(user.getId());
        return new ResponseEntity<>(userShares, HttpStatus.OK);
    }


}
