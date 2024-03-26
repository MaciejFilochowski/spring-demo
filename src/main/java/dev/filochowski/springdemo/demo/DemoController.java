package dev.filochowski.springdemo.demo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String sayHello(Model model, HttpServletRequest request) {
        model.addAttribute("variable", "public");
        model.addAttribute("ip", request.getRemoteAddr());
        model.addAttribute("userAgent", request.getHeader("User-Agent"));
        model.addAttribute("time", new Date());
        return "welcome";
    }
    @GetMapping("secured")
    public String sayHelloSecured(Model model, HttpServletRequest request) {
        model.addAttribute("variable", "secured");
        model.addAttribute("ip", request.getRemoteAddr());
        model.addAttribute("userAgent", request.getHeader("User-Agent"));
        model.addAttribute("time", new Date());
        return "welcome";
    }
}
