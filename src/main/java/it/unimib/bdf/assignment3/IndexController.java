package it.unimib.bdf.assignment3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")    // method handles only GET HTTP requests
    public String index(Model model){
        model.addAttribute("appName", appName);
        return "index"; // this method directs the webpage to the index.html
    }
}
