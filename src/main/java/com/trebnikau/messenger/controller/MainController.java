package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.Message;
import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
            @RequestParam(name = "text") String text,
            @RequestParam(name = "tag") String tag,
            // Поле связанное с файлом
            @RequestParam("file") MultipartFile file,
            Map<String, Object> model) throws IOException {
        Message message = new Message(text, tag, user);

        if (!file.isEmpty()){
            File uploadDir = new File(uploadPath);
//            Если данной папки не существует, то мы её создаём
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
//            Создаём уникальное имя для файла
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
//            Загрузка файла
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFileName(resultFileName);
        }

        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
//        model.put("filter", "");
        return "redirect:/main";
    }


}
