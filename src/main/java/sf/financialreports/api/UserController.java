package sf.financialreports.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @PostMapping("/login")
    public String login(){
        // TODO: jwt token logic
        return "TODO";
    }
}
