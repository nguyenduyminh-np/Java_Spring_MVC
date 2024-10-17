package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    // GET trang giao diện CREATE PRODUCT
    @GetMapping("/admin/product/create") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    // POST trang giao diện CREATE PRODUCT
    @PostMapping(value = "/admin/product/create")
    public String postCreateProductPage(Model model, @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        // Check error validation
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            // Check function BindingResult
            System.out.println(error.getObjectName() + " o day Create Fault here >>>>>>> " +
                    error.getDefaultMessage());
        }
        if (newProductBindingResult.hasErrors()) {
            System.out.println("Check FAULT ON CREATE HERE>>>>>" + newProductBindingResult.getFieldError());
            return "/admin/product/create";
        }

        String image = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image);

        this.productService.createProduct(product);
        return "redirect:/admin/product";
    }

    // GET trang giao diện TABLE PRODUCT
    @GetMapping(value = "admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products1", products);
        return "admin/product/show";
    }

    // GET trang giao diện DETAIL PRODUCT
    @GetMapping("admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable("id") long id) {
        Product product = this.productService.fetchProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    // GET trang giao diện UPDATE PRODUCT
    @GetMapping("admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable("id") long id) {
        Product update_product = this.productService.fetchProductById(id).get();
        model.addAttribute("updateProduct", update_product);
        model.addAttribute("id", id);
        return "admin/product/update";
    }

    // POST trang giao diện UPDATE PRODUCT
    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("updateProduct") @Valid Product product,
            BindingResult updateProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        Product update_product = this.productService.fetchProductById(product.getId()).get();
        List<FieldError> errors = updateProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            // Check function BindingResult
            System.out.println(error.getObjectName() + " o day Update Fault here >>>>>>> " +
                    error.getDefaultMessage());
        }
        if (updateProductBindingResult.hasErrors()) {
            System.out.println("Check FAULT ON UPDATE HERE>>>>>" + updateProductBindingResult.getFieldError());
            return "/admin/product/update";
        }

        if (update_product != null) {

            if (!file.isEmpty()) {
                String image = this.uploadService.handleSaveUploadFile(file, "product");
                update_product.setImage(image);
            }
            System.out.println("Có thể update file >>>>>>>>>");
            update_product.setName(product.getName());
            update_product.setPrice(product.getPrice());
            update_product.setDetailDesc(product.getDetailDesc());
            update_product.setShortDesc(product.getShortDesc());
            update_product.setQuantity(product.getQuantity());
            update_product.setFactory(product.getFactory());
            update_product.setTarget(product.getTarget());
            this.productService.handleSaveProduct(update_product);
        }
        return "redirect:/admin/product";
    }

    // GET trang giao diện DELETE PRODUCT
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    // POST trang giao diện DELETE PRODUCT
    @PostMapping("/admin/product/delete")
    public String postDeletePRoduct(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.deleteById(product.getId());
        return "redirect:/admin/product";
    }

}