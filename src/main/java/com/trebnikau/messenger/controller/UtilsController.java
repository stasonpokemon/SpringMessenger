package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class UtilsController {


    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> fieldErrorMapCollector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        Map<String, String> errorsMap = bindingResult.getFieldErrors().stream().collect(fieldErrorMapCollector);
        return errorsMap;
    }

    static void saveFile(Message message, MultipartFile file, String uploadPath) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
//            Если данной папки не существует, то мы её создаём
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
//            Создаём уникальное имя для файла
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
//            Загрузка файла
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFileName(resultFileName);
        }
    }
}
