package soma.gstbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DevController {
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "hello";
    }

    @GetMapping("/docs")
    public String docs() {
        return "docs/api-docs.html";
    }
}
