package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.Message;
import com.trebnikau.messenger.entity.Role;
import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.repo.MessageRepo;
import com.trebnikau.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @GetMapping("/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditUser(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(@RequestParam("userId") User user,
                           @RequestParam("userName") String userName,
                           @RequestParam Map<String, String> form) {
        userService.saveUser(user, userName, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email) {
        userService.updateProfile(user, password, email);
//        model.addAttribute("infoMessage","Profile is update");
//        return "redirect:/user/profile";
        return "redirect:/main";
    }

    @GetMapping("/messages/{user}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               Model model,
                               @RequestParam(name = "message", required = false) Message message) {
        if (message == null) {
            model.addAttribute("isEdit", false);
        } else {
            model.addAttribute("isEdit", true);
        }
        Set<Message> userMessages = user.getMessages();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", userMessages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/messages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable("user") Long userId,
                                @RequestParam("id") Message message,
                                @RequestParam("text") String text,
                                @RequestParam("tag") String tag,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            UtilsController.saveFile(message, file, uploadPath);
            messageRepo.save(message);
        }
        return "redirect:/user/messages/" + userId;
    }

    @GetMapping("/subscriptions/{user}")
    public String showUserSubscription(@PathVariable("user") User userChannel,
                                       Model model) {
        Set<User> subscriptions = userChannel.getSubscriptions();
        model.addAttribute("users", subscriptions);
        model.addAttribute("userChannel", userChannel);
//        model.addAttribute("subscriptionCount", userChannel.getSubscriptions().size());
//        model.addAttribute("subscribersCount", userChannel.getSubscribers().size());
        return "userList";
    }

    @GetMapping("/subscribers/{user}")
    public String showUserSubscribers(@PathVariable("user") User userChannel,
                                      Model model) {
        Set<User> subscribers = userChannel.getSubscribers();
        model.addAttribute("users", subscribers);
        model.addAttribute("userChannel", userChannel);
//        model.addAttribute("subscriptionCount", userChannel.getSubscriptions().size());
//        model.addAttribute("subscribersCount", userChannel.getSubscribers().size());
        return "userList";
    }

    @GetMapping("/subscribe/{user}")
    public String subscribeToUser(@AuthenticationPrincipal User currentUser,
                                  @PathVariable("user") User user) {
        userService.subscribe(currentUser, user);
        return "redirect:/user/messages/" + user.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribeFromUser(@AuthenticationPrincipal User currentUser,
                                      @PathVariable("user") User user) {
        userService.unsubscribe(currentUser, user);
        return "redirect:/user/messages/" + user.getId();
    }

    @GetMapping("/{type}/{user}/list")
    public String userList(@PathVariable String type,
                           @PathVariable User user,
                           Model model) {
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);
        if ("subscriptions".equals(type)){
            Set<User> subscriptions = user.getSubscriptions();
            model.addAttribute("users", subscriptions);
        }else{
            Set<User> subscribers = user.getSubscribers();
            model.addAttribute("users", subscribers);
        }



        return "subscriptions";
    }
}
