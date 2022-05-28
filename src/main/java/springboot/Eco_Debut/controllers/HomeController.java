package springboot.Eco_Debut.controllers;

import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springboot.Eco_Debut.entities.*;
import springboot.Eco_Debut.repositories.RolesRepository;
import springboot.Eco_Debut.repositories.UserRepository;
import springboot.Eco_Debut.services.ItemService;
import springboot.Eco_Debut.services.UserService;
import springboot.Eco_Debut.services.UserServiceImpl;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.sql.Date;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RolesRepository rolesRepository;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.picture.viewPath}")
    private String viewPathPicture;

    @Value("${file.picture.uploadPath}")
    private String uploadPathPicutre;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;


    @Value("${file.pictureItem.viewPath}")
    private String viewItemPicture;

    @Value("${file.pictureItem.uploadPath}")
    private String uploadItemPicture;

    @Value("${file.pictureItem.defaultPicture}")
    private String defaultItemPicture;

    @Autowired
    private UserRepository userRepository;

    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_PNG = "image/png";


    @GetMapping(value = "/")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String home(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);
        return "home";
    }


    @GetMapping(value = "/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categoryAdmin";
    }


    @GetMapping(value = "/adminType")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminType(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        return "typeAdmin";
    }


    @GetMapping(value = "/roleAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String roleAdmin(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Roles> roles = itemService.getAllRoles();
        model.addAttribute("roles", roles);
        return "roleAdmin";
    }


    @GetMapping(value = "/adminSize")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminSize(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Sizes> sizes = itemService.getAllSizes();
        model.addAttribute("sizes", sizes);
        return "sizeAdmin";
    }


    @GetMapping(value = "/adminItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String adminItem(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);


        return "itemAdmin";
    }


    @GetMapping(value = "/userAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String userAdmin(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Users> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "userAdmin";
    }


    @PostMapping(value = "/addItem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addItem(@RequestParam(name = "item_name", defaultValue = "No Item") String name,
                          @RequestParam(name = "item_description", defaultValue = "No Item") String description,
                          @RequestParam(name = "item_price", defaultValue = "0") double price,
                          @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                          @RequestParam(name = "item_smallPicUrl", defaultValue = "No Item") String smallPicUrl,
                          @RequestParam(name = "item_largePicUrl", defaultValue = "No Item") String largePicUrl,
                          @RequestParam(name = "item_addedDate", defaultValue = "1111-11-11") Date date,
                          @RequestParam(name = "item_inTopPage") boolean inTopPage,
                          @RequestParam(name = "type_id", defaultValue = "0") Long type_id,
                          @RequestParam(name = "category_id", defaultValue = "0") Long category_id) {
        // itemService.addItem(new Items(null,name,description,price,stars,smallPicUrl,largePicUrl,date,inTopPage));
        Types type = itemService.getType(type_id);
        Categories category = itemService.getCategory(category_id);
        if (type != null) {
            Items item = new Items();
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setStars(stars);
            item.setSmallPicURL(smallPicUrl);
            item.setLargePicURL(largePicUrl);
            item.setAddedDate(date);
            item.setInTopPage(inTopPage);
            item.setType(type);
            item.setCategory(category);
            itemService.addItem(item);
        }
        return "redirect:/adminItem";
    }


    @PostMapping(value = "/addCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addItem(@RequestParam(name = "c_name", defaultValue = "No Item") String name) {

        Categories category = new Categories();
        category.setName(name);
        itemService.addCategory(category);

        return "redirect:/admin";
    }


    @PostMapping(value = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "email", defaultValue = "No Item") String email,
                          @RequestParam(name = "password", defaultValue = "No Item") String password,
                          @RequestParam(name = "fullname", defaultValue = "No Item") String fullname) {

        Users user = new Users();
        user.setEmail(email);
        user.setFullName(fullname);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Roles> roles = new ArrayList<>();
        roles.add(rolesRepository.getOne(3L));
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/userAdmin";
    }


    @PostMapping(value = "/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "role_name", defaultValue = "No Item") String name,
                          @RequestParam(name = "role_description", defaultValue = "No Item") String description) {

        Roles role = new Roles();
        role.setRole(name);
        role.setDescription(description);
        itemService.addRole(role);

        return "redirect:/roleAdmin";
    }


    @PostMapping(value = "/addSize")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addSize(@RequestParam(name = "size_name", defaultValue = "No value") String name) {

        Sizes size = new Sizes();
        size.setName(name);
        itemService.addSize(size);

        return "redirect:/adminSize";
    }


    @PostMapping(value = "/addType")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addType(@RequestParam(name = "t_name", defaultValue = "No Item") String name) {

        Types type = new Types();
        type.setName(name);
        itemService.addType(type);

        return "redirect:/adminType";
    }

    @GetMapping(value = "/details/{id}")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String details(Model model, HttpSession session, @PathVariable(name = "id") Long id,
                          @RequestParam(name = "size_required", defaultValue = "false") Boolean sizeRequired) {
        if (sizeRequired) {
            model.addAttribute("size_required_alert", "Size required, please choose!");
        }
        model.addAttribute("current_user", getUserData());
        Items item = itemService.getItem(id);
        model.addAttribute("item", item);
        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Comments> comments = itemService.itemComments(id);
        model.addAttribute("comments", comments);

        List<Sizes> sizes = itemService.getAllSizes();
        model.addAttribute("sizes", sizes);

        List<Pictures> pictures = itemService.findByItem(id);
        model.addAttribute("pictures", pictures);
        return "details";
    }


    @GetMapping(value = "/detailsAdmin/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String detailsAdmin(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("current_user", getUserData());
        Items item = itemService.getItem(id);
        model.addAttribute("item", item);
        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Sizes> sizes = itemService.getAllSizes();
        model.addAttribute("sizes", sizes);
        List<Pictures> pictures = itemService.findByItem(id);
        model.addAttribute("pictures", pictures);
        return "detailsAdmin";
    }


    @GetMapping(value = "/detailsUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String detailsUser(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("current_user", getUserData());
        Users user = userService.getUser(id);
        model.addAttribute("user", user);
        List<Roles> roles = itemService.getAllRoles();
        model.addAttribute("roles", roles);
        ;
        return "detailsUser";
    }


    @PostMapping(value = "/assignsize")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignsize(@RequestParam(name = "item_id", defaultValue = "0") Long item_id,
                             @RequestParam(name = "size_id", defaultValue = "0") Long size_id) {

        Sizes size = itemService.getSize(size_id);
        if (size != null) {
            Items item = itemService.getItem(item_id);
            if (item != null) {
                List<Sizes> sizes = item.getSizes();
                if (sizes == null) {
                    sizes = new ArrayList<>();
                }
                sizes.add(size);
                itemService.saveItem(item);
                return "redirect:/detailsAdmin/" + item_id;
            }

        }
        return "redirect:/";
    }


    @GetMapping(value = "/assigndeletesize")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assigndeletesize(@RequestParam(name = "item_id", defaultValue = "0") Long item_id,
                                   @RequestParam(name = "size_id", defaultValue = "0") Long size_id) {

        Sizes size = itemService.getSize(size_id);
        if (size != null) {
            Items item = itemService.getItem(item_id);
            if (item != null) {
                List<Sizes> sizes = item.getSizes();
                sizes.remove(size);
                itemService.saveItem(item);
                return "redirect:/detailsAdmin/" + item_id;
            }

        }
        return "redirect:/";
    }


    @PostMapping(value = "/editItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String editItem(@RequestParam(name = "item_id", defaultValue = "0") Long id,
                           @RequestParam(name = "item_name", defaultValue = "No Item") String name,
                           @RequestParam(name = "item_description", defaultValue = "No Item") String description,
                           @RequestParam(name = "item_price", defaultValue = "0") double price,
                           @RequestParam(name = "item_stars", defaultValue = "0") int stars,
                           @RequestParam(name = "item_smallPicUrl", defaultValue = "No Item") String smallPicUrl,
                           @RequestParam(name = "item_largePicUrl", defaultValue = "No Item") String largePicUrl,
                           @RequestParam(name = "item_addedDate", defaultValue = "1111-11-11") Date date,
                           @RequestParam(name = "item_inTopPage") boolean inTopPage,
                           @RequestParam(name = "type_id", defaultValue = "0") Long type_id,
                           @RequestParam(name = "category_id", defaultValue = "0") Long category_id) {
        Types type = itemService.getType(type_id);
        Categories category = itemService.getCategory(category_id);
        if (type != null) {
            Items item = itemService.getItem(id);
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setStars(stars);
            item.setSmallPicURL(smallPicUrl);
            item.setLargePicURL(largePicUrl);
            item.setAddedDate(date);
            item.setInTopPage(inTopPage);
            item.setType(type);
            item.setCategory(category);
            itemService.saveItem(item);
        }
        return "redirect:/detailsAdmin/" + id;
    }


    @PostMapping(value = "/editCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editItem(@RequestParam(name = "category_id", defaultValue = "0") Long id,
                           @RequestParam(name = "category_name", defaultValue = "No Item") String name) {
        Categories category = itemService.getCategory(id);
        if (category != null) {
            category.setName(name);
            itemService.saveCategory(category);
        }
        return "redirect:/admin";
    }


    @PostMapping(value = "/editUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(@RequestParam(name = "user_id", defaultValue = "0") Long id,
                           @RequestParam(name = "email", defaultValue = "No Item") String email,
                           @RequestParam(name = "password", defaultValue = "No Item") String password,
                           @RequestParam(name = "fullname", defaultValue = "No Item") String fullname) {
        Users user = userService.getUser(id);
        if (user != null) {
            user.setEmail(email);
            user.setFullName(fullname);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userService.saveUser(user);
        }
        return "redirect:/detailsUser/" + id;
    }


    @PostMapping(value = "/editRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editRole(@RequestParam(name = "role_id", defaultValue = "0") Long id,
                           @RequestParam(name = "role_name", defaultValue = "No Item") String name,
                           @RequestParam(name = "role_description", defaultValue = "No Item") String description) {
        Roles role = itemService.getRole(id);
        if (role != null) {
            role.setRole(name);
            role.setDescription(description);
            itemService.saveRole(role);
        }
        return "redirect:/roleAdmin";
    }


    @PostMapping(value = "/editSize")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editSize(@RequestParam(name = "size_id", defaultValue = "0") Long id,
                           @RequestParam(name = "size_name", defaultValue = "No value") String name) {
        Sizes size = itemService.getSize(id);
        if (size != null) {
            size.setName(name);
            itemService.saveSize(size);
        }
        return "redirect:/adminSize";
    }


    @PostMapping(value = "/editType")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editType(@RequestParam(name = "type_id", defaultValue = "0") Long id,
                           @RequestParam(name = "type_name", defaultValue = "No Item") String name) {

        Types type = itemService.getType(id);
        type.setName(name);
        itemService.saveType(type);

        return "redirect:/adminType";
    }


    @GetMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "item_id", defaultValue = "0") Long id) {
        Items item = itemService.getItem(id);
        if (item != null) {
            itemService.deleteItem(item);
        }
        return "redirect:/adminItem";
    }

    @GetMapping(value = "/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id", defaultValue = "0") Long id) {
        Users user = userService.getUser(id);
        if (user != null) {
            userService.deleteUser(user);
        }
        return "redirect:/userAdmin";
    }


    @PostMapping(value = "/deleteSize")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteSize(@RequestParam(name = "size_id", defaultValue = "0") Long id) {
        Sizes size = itemService.getSize(id);
        if (size != null) {
            itemService.deleteSize(size);
        }
        return "redirect:/adminSize";
    }


    @PostMapping(value = "/deleteRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRole(@RequestParam(name = "role_id", defaultValue = "0") Long id) {
        Roles role = itemService.getRole(id);
        if (role != null) {
            itemService.deleteRole(role);
        }
        return "redirect:/roleAdmin";
    }


    @PostMapping(value = "/deleteCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam(name = "c_id", defaultValue = "0") Long id) {
        Categories category = itemService.getCategory(id);
        if (category != null) {
            itemService.deleteCategory(category);
        }
        return "redirect:/admin";
    }


    @PostMapping(value = "/deleteType")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteType(@RequestParam(name = "type_id", defaultValue = "0") Long id) {
        Types type = itemService.getType(id);
        if (type != null) {
            itemService.deleteType(type);
        }
        return "redirect:/adminType";
    }


    @GetMapping(value = "/search")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String search(Model model, @RequestParam(name = "item_name", defaultValue = "No data") String name) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.searchByName(name);
        if (items != null) {
            model.addAttribute("items", items);
            if (!items.isEmpty()) {
                Types type = itemService.getType(items.get(0).getType().getId());
                model.addAttribute("type", type);
            }
        }
        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        return "search";
    }


    @GetMapping(value = "/searchDetails")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String searchDetails(Model model, @RequestParam(name = "item_name", defaultValue = "No data") String name,
                                @RequestParam(name = "item_pricefrom", defaultValue = "0") double pricefrom,
                                @RequestParam(name = "item_priceto", defaultValue = "9000000") double priceto,
                                @RequestParam(name = "options", defaultValue = "ascending") String option,
                                @RequestParam(name = "type_id", defaultValue = "0") Long type_id) {

        List<Items> items = itemService.searchByNameAndTypeOrderByPriceAsc(name, type_id);
        model.addAttribute("current_user", getUserData());
        if (name.equals("No data") && pricefrom == 0.0 && priceto == 9000000.0) {
            if (option.equals("descending")) {
                items = itemService.serachByTypeOrderByPriceDesc(type_id);
            } else {
                items = itemService.serachByTypeOrderByPriceAsc(type_id);
            }
        } else {

            if (pricefrom != 0.0 && priceto != 9000000.0) {
                if (option.equals("descending")) {
                    items = itemService.searchByNameAndPriceBetweenAndTypeOrderByPriceDesc(name, pricefrom, priceto, type_id);
                } else {
                    items = itemService.searchByNameAndPriceBetweenAndTypeOrderByPriceAsc(name, pricefrom, priceto, type_id);
                }
            } else {
                if (option.equals("descending")) {
                    items = itemService.searchByNameAndTypeOrderByPriceDesc(name, type_id);
                }
            }
        }

        if (items != null) {
            model.addAttribute("items", items);
        }
        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        Types type = itemService.getType(type_id);
        model.addAttribute("type", type);
        return "search";
    }


    ///////////////////////


    @GetMapping(value = "/403")
    public String accessDenied(Model model) {
        model.addAttribute("current_user", getUserData());
        return "403";
    }


    @GetMapping(value = "/login")
    public String login(Model model, HttpSession session) {
//        session.removeAttribute("busket");
//        model.addAttribute("current_user", getUserData());
//
//        List<Items> items = itemService.getAllItems();
//        model.addAttribute("items", items);
//
//        List<Types> types = itemService.getAllTypes();
//        model.addAttribute("types", types);
//
//        List<Categories> categories = itemService.getAllCategories();
//        model.addAttribute("categories", categories);


        return "login";
    }

//    @PostMapping(value = "/auth")
//    public String auth(@RequestParam(value = "user_email") String email,
//                       @RequestParam(value = "user_password") String password){
//        Users users = userService.getUserByEmail(email);
//        if(Objects.isNull(users)){
//            return "redirect:/login?error";
//        }
//        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
//        if (!bCrypt.matches(password, users.getPassword())) {
//            return "redirect:/login?error";
//        }
//        if (!users.getEnabled()) {
//            return "redirect:/login?notEnabled";
//        }
//        return "redirect:/profile";
//    }


    @PostMapping(value = "/registr")
    public String registr(Model model, @RequestParam(name = "user_email", defaultValue = "No data") String email,
                          @RequestParam(name = "user_password", defaultValue = " ") String password,
                          @RequestParam(name = "user_repassword", defaultValue = " ") String re_password,
                          @RequestParam(name = "user_fullname", defaultValue = "") String fullname,HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Users user = userService.getUserByEmail(email);


        if (user != null) {
            return  "redirect:/registration?userExist";
        } else {
            if (!password.equals(re_password)) {
                return  "redirect:/registration?password";
            } else {
                user = new Users();
                user.setEmail(email);
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(password));
                System.out.println(bCryptPasswordEncoder.encode(password));
                user.setFullName(fullname);
                List<Roles> roles = new ArrayList<>();
                roles.add(rolesRepository.getOne(3L));
                String randomCode = RandomString.make(64);
                user.setVerificationCode(randomCode);
                user.setEnabled(false);
                user.setRoles(roles);
                userService.saveUser(user);
                userService.sendVerificationEmail(user,getSiteURL(request));
//                model.addAttribute("success", "User registered successfully");

                return "successRegistration";
            }
        }
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public boolean verify(String verificationCode) {
        Users user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        if (verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("current_user", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);


        return "registration";
    }


    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        Users users =  getUserData();
        if (!users.getEnabled()) {
            return "redirect:/login?notEnabled";
        }
        model.addAttribute("current_user",users);

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "profile";
    }


    @PostMapping(value = "/updateProfile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(Model model, @RequestParam(name = "email", defaultValue = "0") String email,
                                @RequestParam(name = "fullname", defaultValue = "No value") String fullname,
                                @RequestParam(name = "address", defaultValue = "No value") String address) {
        Users user = userService.getUserByEmail(email);
        if (user != null) {
            user.setFullName(fullname);
            user.setAddress(address);
            userService.saveUser(user);


            return "redirect:/profile?success";
        }
        return "redirect:/profile";
    }


    @PostMapping(value = "/updatePassword")
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(Model model, @RequestParam(name = "email", defaultValue = "0") String email,
                                 @RequestParam(name = "oldpass", defaultValue = "No value") String oldpass,
                                 @RequestParam(name = "newpass", defaultValue = "No value") String newpass,
                                 @RequestParam(name = "repass", defaultValue = "No value") String repass) {
        Users user = userService.getUserByEmail(email);
        model.addAttribute("current_user", getUserData());
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        if (!bCrypt.matches(oldpass, user.getPassword())) {
            model.addAttribute("message", "Wrong Old Password");
        } else {
            if (newpass.equals(repass)) {
                model.addAttribute("success", "Password Changed Successfully");
                user.setPassword(bCrypt.encode(newpass));
                userService.saveUser(user);
            } else {
                model.addAttribute("message", "Wrong Retype Password");
            }
        }

        return "profile";
    }


    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }


    @PostMapping(value = "/assignrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignrole(@RequestParam(name = "user_id", defaultValue = "0") Long user_id,
                             @RequestParam(name = "role_id", defaultValue = "0") Long role_id) {

        Roles role = itemService.getRole(role_id);
        if (role != null) {
            Users user = userService.getUser(user_id);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                if (roles == null) {
                    roles = new ArrayList<>();
                }
                roles.add(role);
                userService.saveUser(user);
                return "redirect:/detailsUser/" + user_id;
            }

        }
        return "redirect:/";
    }


    @GetMapping(value = "/assigndeleterole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assigndeleterole(@RequestParam(name = "user_id", defaultValue = "0") Long user_id,
                                   @RequestParam(name = "role_id", defaultValue = "0") Long role_id) {

        Roles role = itemService.getRole(role_id);
        if (role != null) {
            Users user = userService.getUser(user_id);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                roles.remove(role);
                userService.saveUser(user);
                return "redirect:/detailsUser/" + user_id;
            }

        }
        return "redirect:/";
    }


    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile file) {
        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            String extension;
            switch (file.getContentType()) {
                case IMAGE_PNG:
                    extension = ".png";
                    break;
                default:
                    extension = ".jpg";
            }
            try {
                Users user = getUserData();
                String picName = DigestUtils.sha1Hex("avatar_" + user.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + extension);
                Files.write(path, bytes);

                user.setPictureURL(picName + extension);
                userService.saveUser(user);
                return "redirect:/profile?successPicture";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/profile";
    }

    @GetMapping(value = "/viewPhoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {
        String pictureURL = viewPath + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPath + url;
        }

        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "/uploadPicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String uploadPicture(@RequestParam(name = "item_picture") MultipartFile file,
                                @RequestParam(name = "item_id") Long itemId) {
        if (Objects.equals(file.getContentType(), "image/jpeg") || file.getContentType().equals("image/png")) {
            String extension;
            switch (file.getContentType()) {
                case IMAGE_PNG:
                    extension = ".png";
                    break;
                default:
                    extension = ".jpg";
            }
            try {
                Items item = itemService.getItem(itemId);
                String picName = DigestUtils.sha1Hex("picture_" + System.currentTimeMillis() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicutre + picName + extension);
                Files.write(path, bytes);


                Pictures picture = new Pictures();

                picture.setUrl(picName);
                picture.setExtension(extension);
                picture.setItem(item);
                Date date = new Date(System.currentTimeMillis());
                picture.setAddedDate(date);

                itemService.addPicture(picture);
                return "redirect:/detailsAdmin/" + itemId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/adminItem";
    }


    @GetMapping(value = "/viewPicture", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public @ResponseBody
    byte[] viewItemPicture(@RequestParam(name = "url") String url) throws IOException {

        Pictures pictures = itemService.getPictureByUrl(url);
        String pictureURL = viewPath + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPathPicture + url + pictures.getExtension();
        }


        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "/viewPictureItem", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public @ResponseBody
    byte[] viewItemPictureItem(@RequestParam(name = "url") String url) throws IOException {

        Pictures pictures = itemService.getPictureByUrl(url);
        String pictureURL = viewPath + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPathPicture + url + pictures.getExtension();
        }


        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "/deletePicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deletePicture(@RequestParam(name = "pic_id", defaultValue = "0") Long pic_id) {
        Pictures picture = itemService.getPicture(pic_id);
        Long item_id = picture.getItem().getId();
        if (picture != null) {
            itemService.deletePicture(picture);
            return "redirect:/detailsAdmin/" + item_id;
        }
        return "redirect:/";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @GetMapping(value = "/addToBasket")
//    @PreAuthorize("isAuthenticated()")
//    public String addToBasket(HttpSession session,Model model,@RequestParam(name = "item_id")Long id){
//        if(session.getAttribute("busket")==null){
//            List<Purchases> busket = new ArrayList<>();
//            Items item = itemService.getItem(id);
//            Purchases p = new Purchases();
//            p.setPrice((int) item.getPrice());
//            p.setItem(item);
//            p.setAmount(1);
//            busket.add(p);
//            session.setAttribute("busket",busket);
//        }else {
//            Items item = itemService.getItem(id);
//            List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
//            Purchases purchase = null;
//            for (Purchases p : busket) {
//                if (p.getItem().getId() == id) {
//                    purchase = p;
//                    break;
//                }
//            }
//            if(purchase==null){
//                purchase = new Purchases();
//                purchase.setItem(item);
//                purchase.setPrice((int) item.getPrice());
//                purchase.setAmount(1);
//                busket.add(purchase);
//            }
//            else{
//                purchase.setAmount(purchase.getAmount()+1);
//                purchase.setPrice((int) (purchase.getPrice()+item.getPrice()));
//            }
//            session.setAttribute("busket",busket);
//        }
//
//
//        return "redirect:/busket";
//    }


    @GetMapping(value = "/addToBasket")
    @PreAuthorize("isAuthenticated()")
    public String addToBasket(HttpSession session, Model model, @RequestParam(name = "item_id") Long id,
                              @RequestParam(name = "size_id") Long size_id) {
        if (Objects.isNull(size_id)) {
            return "redirect:/details/" + id + "?size_required=true";
        }
        if (session.getAttribute("busket") == null) {
            List<Purchases> busket = new ArrayList<>();
            Items item = itemService.getItem(id);
            Sizes sizes1 = itemService.getSize(size_id);
            Purchases p = new Purchases();
            p.setSize(sizes1.getName());
            p.setPrice((int) item.getPrice());
            p.setItem(item);
            item.setSizes(Collections.singletonList(sizes1));
            p.setAmount(1);
            busket.add(p);
            session.setAttribute("busket", busket);
        } else {
            Items item = itemService.getItem(id);
            Sizes sizes1 = itemService.getSize(size_id);
            List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
            Purchases purchase = null;
            for (Purchases p : busket) {
                if (p.getItem().getId() == id && p.getItem().getSizes().contains(sizes1)) {
                    purchase = p;
                    break;
                }
            }
            if (purchase == null) {
                purchase = new Purchases();
                purchase.setItem(item);
                purchase.setSize(sizes1.getName());
                purchase.getItem().setSizes(Collections.singletonList(sizes1));
                purchase.setPrice((int) item.getPrice());
                purchase.setAmount(1);
                busket.add(purchase);
            } else {

                if (purchase.getItem().getSizes().contains(sizes1)) {
                    purchase.setAmount(purchase.getAmount() + 1);
                    purchase.setSize(sizes1.getName());
                    purchase.setPrice((int) (purchase.getPrice() + item.getPrice()));
                } else {
                    purchase = new Purchases();
                    item.setSizes(Collections.singletonList(sizes1));
                    purchase.setItem(item);
                    purchase.setSize(sizes1.getName());
                    purchase.getItem().setSizes(Collections.singletonList(sizes1));
                    purchase.setPrice((int) item.getPrice());
                    purchase.setAmount(1);
                    busket.add(purchase);
                }

            }
            session.setAttribute("busket", busket);
        }


        return "redirect:/busket";
    }


//    @GetMapping(value = "/deleteFromBasket")
//    @PreAuthorize("isAnonymous() || isAuthenticated()")
//    public String deleteFromBasket(HttpSession session,Model model,@RequestParam(name = "item_id")Long id){
//
//            Items item = itemService.getItem(id);
//            List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
//            Purchases purchase = null;
//            for (Purchases p : busket) {
//                if (p.getItem().getId() == id) {
//                    purchase = p;
//                    break;
//                }
//            }
//            if(purchase.getAmount()==1){
//                busket.remove(purchase);
//            }
//            else{
//                purchase.setAmount(purchase.getAmount()-1);
//                purchase.setPrice((int) (purchase.getPrice()-item.getPrice()));
//            }
//            session.setAttribute("busket",busket);
//
//
//
//        return "redirect:/busket";
//    }


    @GetMapping(value = "/deleteFromBasket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String deleteFromBasket(HttpSession session, Model model, @RequestParam(name = "item_id") Long id,
                                   @RequestParam(name = "size_id") Long size_id) {

        Items item = itemService.getItem(id);
        List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
        Sizes sizes1 = itemService.getSize(size_id);
        Purchases purchase = null;
        for (Purchases p : busket) {
            if (p.getItem().getId() == id && p.getItem().getSizes().contains(sizes1)) {
                purchase = p;
                break;
            }
        }
        if (purchase.getAmount() == 1) {
            busket.remove(purchase);
        } else {
            purchase.setAmount(purchase.getAmount() - 1);
            purchase.setPrice((int) (purchase.getPrice() - item.getPrice()));
        }
        session.setAttribute("busket", busket);


        return "redirect:/busket";
    }


    @GetMapping(value = "/busket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String busket(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("current_user", getUserData());
        if (request.getParameter("success") != null) {
            model.addAttribute("success", "The products were successfully purchased");
        }
        if (request.getParameter("error") != null) {
            model.addAttribute("error", "You didn't select any products");
        }

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);


        return "busket";
    }


    @GetMapping(value = "/clearBasket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String clearBasket(Model model, HttpSession session) {
        session.removeAttribute("busket");
        return "redirect:/busket";
    }


    @PostMapping(value = "/checkIn")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String checkIn(Model model, RedirectAttributes redirectAttributes, HttpSession session,
                          @RequestParam(name = "fullname") String fullname
    ) {
        List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
        Users user = getUserData();
        if (busket != null) {
            for (Purchases purchases : busket) {
                purchases.setBuyer(fullname);
                purchases.setAddress(user.getAddress());
                purchases.setDate(new Date(System.currentTimeMillis()));
                itemService.addPurchase(purchases);
            }
            session.removeAttribute("busket");
            return "redirect:/busket?success=success";

        } else {
            return "redirect:/busket?error=error";

        }

    }


    @GetMapping(value = "/soldItemAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String soldItemAdmin(Model model) {
        model.addAttribute("current_user", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Types> types = itemService.getAllTypes();
        model.addAttribute("types", types);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Purchases> purchases = itemService.getAllPurchases();
        model.addAttribute("purchases", purchases);


        return "soldItemsAdmin";
    }


    @PostMapping(value = "/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "comment", defaultValue = " ") String com,
                             @RequestParam(name = "item_id", defaultValue = "0") Long id) {

        Items item = itemService.getItem(id);
        if (item != null) {
            Comments comment = new Comments();
            comment.setAuthor(getUserData());
            comment.setComment(com);
            comment.setItem(item);
            comment.setDate(new java.util.Date(System.currentTimeMillis()));
            itemService.addComment(comment);

        }

        return "redirect:/details/" + id;
    }


    @PostMapping(value = "/edit_Comment")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@RequestParam(name = "comment", defaultValue = " ") String com,
                              @RequestParam(name = "id", defaultValue = "0") Long id) {

        Comments comment = itemService.getComment(id);
        if (comment != null) {
            comment.setComment(com);
            itemService.saveComment(comment);
            return "redirect:/details/" + comment.getItem().getId();
        }

        return "redirect:/";
    }


    @GetMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@RequestParam(name = "id", defaultValue = "0") Long id) {

        Comments comment = itemService.getComment(id);
        Long item_id = comment.getItem().getId();
        if (comment != null) {
            itemService.deleteComment(comment);
            return "redirect:/details/" + item_id;
        }

        return "redirect:/";
    }


    @PostMapping(value = "/uploadPictureItem")
    public String uploadPictureItem(@RequestParam(name = "item_picture") MultipartFile file,
                                    @RequestParam(name = "item_id") Long itemId) {
        System.out.println(file.getContentType());
        System.out.println(file.getContentType());
        System.out.println(file.getContentType());
        System.out.println(file.getContentType());
        System.out.println(file.getContentType());
        System.out.println(file.getContentType());
        if (Objects.equals(file.getContentType(), "image/jpeg") ||Objects.equals(file.getContentType(), "image/jpg") || file.getContentType().equals("image/png")) {
            String extension;
            switch (file.getContentType()) {
                case IMAGE_PNG:
                    extension = ".png";
                    break;
                default:
                    extension = ".jpg";
            }
            try {
                Items item = itemService.getItem(itemId);
                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                System.out.println("PAAAAAAAATH");
                System.out.println("PAAAAAAAATH");
                File file2 = new File(uploadItemPicture + picName + extension);
                System.out.println("PAAAAAAAATH");

                file.transferTo(file2);
                System.out.println("PAAAAAAAATH");
                System.out.println("PAAAAAAAATH");
                System.out.println("file naame"+file2.getName());
                System.out.println("file naame"+file2.getName());
                System.out.println("file naame"+file2.getName());

                item.setSmallPicURL(file.getOriginalFilename());

                itemService.saveItem(item);
                return "redirect:/detailsAdmin/" + itemId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/adminItem";
    }
}
