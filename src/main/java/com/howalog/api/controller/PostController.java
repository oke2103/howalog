package com.howalog.api.controller;

import com.howalog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/post")
    public Map<String, String> post(@RequestBody @Valid PostCreate param) {
        log.info("param = {}", param);
        return Map.of();
    }
}
