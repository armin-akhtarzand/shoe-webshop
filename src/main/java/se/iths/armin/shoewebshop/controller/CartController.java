package se.iths.armin.shoewebshop.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.iths.armin.shoewebshop.entity.Cart;
import se.iths.armin.shoewebshop.entity.CustomerOrder;
import se.iths.armin.shoewebshop.service.CartService;
import se.iths.armin.shoewebshop.service.OrderService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }


    @GetMapping
    public String showCart(Model model, HttpSession session) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }


    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable Long id, HttpSession session) {
        cartService.addProduct(session, id);
        if ("/cart".equals(session.getServletContext().getContextPath())) {
            return "redirect:/cart";
        }
        return "redirect:/products";

    }

    @PostMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeProduct(session, id);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Cart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        CustomerOrder order = orderService.checkout(username, cart);
        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/cart/confirmation";
    }

    @GetMapping("/confirmation")
    public String confirmation(Model model) {
        if (!model.containsAttribute("order")) {
            return "redirect:/cart";
        }
        return "confirmation";
    }


}
