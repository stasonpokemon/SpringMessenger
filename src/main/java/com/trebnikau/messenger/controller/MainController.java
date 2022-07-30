package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.Message;
import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, name = "filter", defaultValue = "") String filter,
                       Model model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findAllByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String addMessage(
            // В эту аннотацию попадает user, который добавляет сообщение
            @AuthenticationPrincipal User user,
//            @RequestParam(name = "text") String text,
//            @RequestParam(name = "tag") String tag,
            @Valid Message message,
            BindingResult bindingResult, // хранит в себе список аргументов и сообщения ошибок валидации(должен стоять перед model)
            Model model,
            // Поле связанное с файлом
            @RequestParam("file") MultipartFile file
    ) throws IOException {
//        Message message = new Message(text, tag, user);

        message.setAuthor(user);
        // Если у нас есть ошибки, то сообщение не созранится
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            UtilsController.saveFile(message, file, uploadPath);
            // если валидация прошла успешно нужно удалить message из model, иначе, после добавления мы опять получим открытую форму с сообщением, которое мы пытались добавить
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
//        model.put("filter", "");

        return "main";
    }


//    @GetMapping("/user-messages/{user}")
//    public String userMessages(@AuthenticationPrincipal User currentUser,
//                               @PathVariable User user,
//                               Model model,
//                               @RequestParam(name = "message", required = false) Message message) {
//        Set<Message> userMessages = user.getMessages();
//        model.addAttribute("messages", userMessages);
//        model.addAttribute("message", message);
//        model.addAttribute("isCurrentUser", currentUser.equals(user));
//        return "userMessages";
//    }
//
//    @PostMapping("/user-messages/{user}")
//    public String updateMessage(@AuthenticationPrincipal User currentUser,
//                                @PathVariable("user") Long userId,
//                                @RequestParam("id") Message message,
//                                @RequestParam("text") String text,
//                                @RequestParam("tag") String tag,
//                                @RequestParam("file") MultipartFile file) throws IOException {
//        if (message.getAuthor().equals(currentUser)) {
//            if (!StringUtils.isEmpty(text)) {
//                message.setText(text);
//            }
//            if (StringUtils.isEmpty(tag)) {
//                message.setTag(tag);
//            }
//            saveFile(message, file);
//            messageRepo.save(message);
//        }
//        return "redirect:/user-messages/" + userId;
//    }

}
