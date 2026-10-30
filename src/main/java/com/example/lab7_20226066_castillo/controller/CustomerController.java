package com.example.lab7_20226066_castillo.controller;

import com.example.lab7_20226066_castillo.entity.customer;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.pucp.gtics.lab7.entity.Customer;
import pe.edu.pucp.gtics.lab7.repository.CustomerRepository;

import java.util.Optional;

/** CRUD de clientes basado en JpaRepository y @ModelAttribute. */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final customerRepository customerRepository;

    // Inyección por constructor, como se practica en clase.
    public CustomerController(customerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping({"", "/list"})
    public String listar(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customer/list";
    }

    @GetMapping("/new")
    public String nuevo(@ModelAttribute("customer") customer customer) {
        // Un mismo archivo form.html permite crear o editar.
        return "customer/form";
    }

    @GetMapping("/edit")
    public String editar(@RequestParam("id") Integer id,
                         @ModelAttribute("customer") customer customer,
                         Model model,
                         RedirectAttributes attributes) {
        Optional<customer> encontrado = customerRepository.findById(id);
        if (encontrado.isEmpty()) {
            attributes.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/customer";
        }
        model.addAttribute("customer", encontrado.get());
        return "customer/form";
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("customer") customer customer,
                          BindingResult bindingResult,
                          RedirectAttributes attributes) {
        // La validación de formato depende del tipo elegido, no solo de @NotBlank.
        if (customer.getName() != null) customer.setName(customer.getName().trim());
        if (customer.getDocument() != null) customer.setDocument(customer.getDocument().trim());

        String tipo = customer.getDocumentType();
        String numero = customer.getDocument();
        if (tipo != null && !tipo.isBlank() && !tipo.equals("DNI") && !tipo.equals("RUC")) {
            bindingResult.rejectValue("documentType", "tipo.invalido", "El tipo debe ser DNI o RUC");
        }
        if (numero != null && !numero.isBlank()) {
            if ("DNI".equals(tipo) && !numero.matches("\\d{8}")) {
                bindingResult.rejectValue("document", "documento.dni", "El DNI debe contener exactamente 8 dígitos numéricos");
            } else if ("RUC".equals(tipo) && !numero.matches("\\d{11}")) {
                bindingResult.rejectValue("document", "documento.ruc", "El RUC debe contener exactamente 11 dígitos numéricos");
            }
            // Validación de unicidad al crear y al editar (permite su propio ID).
            customerRepository.findByDocument(numero).ifPresent(otro -> {
                if (!otro.getId().equals(customer.getId())) {
                    bindingResult.rejectValue("document", "documento.duplicado", "Ya existe un cliente con ese documento");
                }
            });
        }

        if (bindingResult.hasErrors()) return "customer/form";

        try {
            // save() inserta o actualiza según el ID del objeto.
            customerRepository.save(customer);
            attributes.addFlashAttribute("msg", "Cliente guardado correctamente");
            return "redirect:/customer";
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("document", "documento.duplicado", "El documento ya está registrado");
            return "customer/form";
        }
    }

    @PostMapping("/delete")
    public String eliminar(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        if (!customerRepository.existsById(id)) {
            attributes.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/customer";
        }
        try {
            customerRepository.deleteById(id);
            customerRepository.flush(); // Fuerza la comprobación de relaciones de MySQL.
            attributes.addFlashAttribute("msg", "Cliente eliminado correctamente");
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("error", "No se puede eliminar: el cliente tiene comprobantes asociados");
        }
        return "redirect:/customer";
    }
}