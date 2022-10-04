package com.ecommerce.admin.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.imp.AdminServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private AdminServiceImp adminServiceImp;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title", "login");
        return "login";
    }

    // SecurityContextHolder 是用來保存 SecurityContext 的，通過 SecurityContextHolder.getContext() 靜態方法可以獲得當前 SecurityContext 對象。
    @RequestMapping("/index")
    public String home(Model model){
        Model model1 = model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }


    // Model物件來傳送java物件給jsp，通常呼叫addAttribute(arg1, arg2)，第一個變數是要傳到jsp的變數名稱，第二個變數是對應的java物件
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title", "Forgot Password");
        return  "forgot-password";
    }


    /**
       在方法引數上宣告@valid註解,校驗通過，才會執行業務邏輯處理
       使用 model.addAttribute(String key, Object value); 來向 Model 中增加引數
       一般被 @ModelAttribute 註解的無返回值控制器方法被用來向 Model 物件中設定引數，在控制器跳轉的頁面中可以取得 Model 中設定的引數
     */
    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
                              BindingResult result,
                              Model model){
        try {
            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminServiceImp.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("emailError", "Your email has been registered!");
                return  "register";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(bCryptPasswordEncoder.encode(adminDto.getPassword()));
                adminServiceImp.save(adminDto);
                model.addAttribute("success", "Register successfully");
                model.addAttribute("adminDto", adminDto);
            }else{
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again");
                return "register";
            }
        }

        catch(Exception e){
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong!");
        }

        return "register";

    }
}
