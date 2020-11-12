package com.night.filter.controller;

import com.night.filter.domain.RetResult;
import com.night.filter.domain.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secret")
public class SecretController {



    @RequestMapping("/get")
    public RetResult<String> getMessage(@RequestBody User user){

        RetResult<String> result = new RetResult<>();

        if(user.getName().equalsIgnoreCase("night") ){

            result.setData("yes you get it (没问题)");
        }else {
            result.setData("no try again  (再来一次)");
        }
        return  result;
    }
}
