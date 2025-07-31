package br.com.apibroka.broka.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Somente teste")
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public String check(){
        return "Api do broka ta rodando";
    }
}
