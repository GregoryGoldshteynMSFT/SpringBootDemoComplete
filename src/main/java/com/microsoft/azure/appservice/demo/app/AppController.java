package com.microsoft.azure.appservice.demo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import jakarta.annotation.PostConstruct;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private boolean dbConnectionFailed = false;

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/logmessage")
    public String logmessage() {
        logger.info("Logged message!");
        return "Logged message!";
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        if(dbConnectionFailed)
        {
            logger.error("DB connection failed. Could not find TODOs");
            return null;
        }
        return todoRepository.findAll();
    }

    @PostConstruct
    public void checkDatabaseConnection() {
        try {
            todoRepository.count(); // Try to perform a simple operation on the DB
            logger.info("Database connected successfully.");
        } catch (DataAccessException ex) {
            logger.error("Cannot connect to the database. Switching to fallback mode.", ex);
            dbConnectionFailed = true;
        }
    }
}