package org.example.todointership;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class toDocontroller {
    @GetMapping()
    public  String hello(){
        return "{ \"name\": \"Task API\", \"version\": \"1.0\", \"endpoints\": [\"/tasks\"] }";
    }

    @GetMapping("/health")
    public String health() {
        return "{ \"status\": \"ok\" }";  }
}
