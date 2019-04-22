package com.sunchs.lyt.question;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sunchs.lyt.db","com.sunchs.lyt.question"})
public class QuestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestionApplication.class, args);
    }
}