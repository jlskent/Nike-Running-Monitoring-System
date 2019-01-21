package com.example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//flag for controller
@RestController
public class HelloWorldRestController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloWorld(){
        return "hihihi";
    }
}
