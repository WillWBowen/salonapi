package com.salon.www.salonapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static Logger logger = LogManager.getLogger();
    @RequestMapping("/home")
    public String getHome() {

        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warn message.");
        logger.error("This is an error message.");
        logger.fatal("This is a fatal message.");
        return "Hello from Java";
    }
}
