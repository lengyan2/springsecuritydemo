package com.example.springsecurity03.controller;

import com.google.code.kaptcha.Producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class KaptchaController {

    private  final Producer producer;
    private  final RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public KaptchaController(Producer producer,RedisTemplate<String,Object> redisTemplate1){
        this.producer = producer;
        this.redisTemplate = redisTemplate1;
    }

    @GetMapping("kaptcha.jpg")
    public String getKaptcha(HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        String code = producer.createText();
        redisTemplate.opsForValue().set("kaptcha",code);
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"png",byteArrayOutputStream);
        String s = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        return s;
    }
}
