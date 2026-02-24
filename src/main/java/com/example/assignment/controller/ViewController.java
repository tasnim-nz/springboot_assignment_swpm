package com.example.assignment.controller;

import com.example.assignment.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final DashboardService dashboardService; // Inject Service

    public ViewController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        // Service handles authentication via SecurityContextHolder
        model.addAttribute("students", dashboardService.getStudentsForUser());
        model.addAttribute("courses", dashboardService.getAllCourses());

        return "dashboard";
    }
}