package vn.hoidanit.laptopshop.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserService userService;
    private final UploadService uploadService;

    public UserController(UserService userService, UploadService uploadService) {
        this.uploadService = uploadService;
        this.userService = userService;
    }

    // GET Trang chủ
    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUsersByEmail("duyminh@gmail.com");
        System.out.println(arrUsers);

        model.addAttribute("ericNotHTML",
                "Đây là content ánh xạ từ key ericNotHTML. Đối tượng Model sẽ render view từ controller");
        model.addAttribute("duyminhdeptrai", "From Controller, Đây là content ánh xạ từ key duyminhdeptrai ");
        return "hello";
    }

    // GET trang giao diện TABLE-USER
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }

    // GET trang giao diện USER_DETAIL
    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    // GET trang giao diện CREATE USER
    @GetMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // POST trang giao diện CREATE USER
    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit,
            @RequestParam("hoidanitFile") MultipartFile[] files) {
        List<String> avatar = this.uploadService.handleSaveUploadFile(files, "avatar");

        // this.userService.handleSaveUser(hoidanit);
        return "redirect:/admin/user";
    }

    // GET trang giao diện UPDATE USER
    @RequestMapping(value = "admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    // POST trang giao diện UPDATE USER
    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        User currentUser = this.userService.getUserById(hoidanit.getId());
        if (currentUser != null) {
            System.out.println("run here at update:>>>>>>>>>> ");
            currentUser.setAddress(hoidanit.getAddress());
            currentUser.setFullName(hoidanit.getFullName());
            currentUser.setPhone(hoidanit.getPhone());
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    // GET trang giao diện DELETE USER
    @RequestMapping(value = "admin/user/delete/{id}") // GET
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        User user_delete = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("user_delete", user_delete);

        model.addAttribute("newUser", user_delete);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User eric) {
        System.out.println("run here at delelte >>>>>>>");
        this.userService.deleteAUser(eric.getId());
        return "redirect:/admin/user";
    }

}
