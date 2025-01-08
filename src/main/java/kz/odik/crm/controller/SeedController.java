package kz.odik.crm.controller;

import kz.odik.crm.Service.SeedService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/create")
public class SeedController {
    @Autowired
    private SeedService seedService;

    @PostMapping("/seed")
    public String seed() {
        System.out.println("3");
        return seedService.seed();

    }

}
