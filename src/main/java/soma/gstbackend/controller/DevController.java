package soma.gstbackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/loginedUser")
    @ResponseBody
    public String logined() {return "loginedUser";}

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping("/guest")
    @ResponseBody
    public String only_guest() { return "guest";}

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    @ResponseBody
    public String only_user() { return "user";}

    @PreAuthorize("hasRole('ROLE_SUPER_USER')")
    @GetMapping("/super-user")
    @ResponseBody
    public String only_super_user() { return "super-user";}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    @ResponseBody
    public String only_admin() { return "admin";}
}
