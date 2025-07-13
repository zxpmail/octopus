package org.zhouxp.octopus.framework.test.plus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhouxp.octopus.framework.test.plus.model.User;
import org.zhouxp.octopus.framework.test.plus.service.UserService;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 17:47:12
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<User> all(){
        return  userService.list();

    }

    @GetMapping("/info/{id}")
    public User info(@PathVariable("id") Long id){
        return userService.getById(id);
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody User user){
        return userService.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id){
        return userService.removeById(id);
    }

}
