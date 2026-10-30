package com.example.lab7_20226066_castillo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Controlador que muestra la página de inicio. */
@Controller
public class HomeController {
    @GetMapping("/")
    public String inicio() {
        // Spring busca index.html dentro de resources/templates.
        return "index";
    }
}
