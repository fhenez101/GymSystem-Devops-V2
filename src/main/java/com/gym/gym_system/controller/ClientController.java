package com.gym.gym_system.controller;

import com.gym.gym_system.entity.Client;
import com.gym.gym_system.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("client", new Client()); // 🔥 IMPORTANTE
        return "clients/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping
    public String saveClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editClient(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));
        model.addAttribute("client", client);
        return "clients/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }
}