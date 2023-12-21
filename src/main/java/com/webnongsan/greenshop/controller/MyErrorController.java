package com.webnongsan.greenshop.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        // do something like logging
        return "web/notFound";
    }
}
