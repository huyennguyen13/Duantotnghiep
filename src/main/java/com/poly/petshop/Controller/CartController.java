package com.poly.petshop.Controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Service.SanPhamService;
import com.poly.petshop.dto.CartItems;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private SanPhamDao sanPhamRepository;

    @Autowired
    private SanPhamService sanPhamService;


    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add/{sanPhamId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addProductToCart(
            @PathVariable Integer sanPhamId,
            @RequestParam int soLuong,
            HttpSession session) {

        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");

        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }

        SanPhamEntity product = sanPhamService.getSanPhamById(sanPhamId);
        if (product == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sản phẩm không tồn tại!");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<CartItems> existingItem = cartItems.stream()
                .filter(item -> item.getSanPhamId().equals(sanPhamId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setSoLuong(existingItem.get().getSoLuong() + soLuong);
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setSanPhamId(product.getSanPhamId());
            cartItem.setTenSanPham(product.getTenSanPham());
            cartItem.setGiaBan(product.getGiaBan());
            cartItem.setSoLuong(soLuong);
            cartItems.add(cartItem);
        }

        session.setAttribute("cartItems", cartItems);

        // Tính tổng tiền giỏ hàng
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getGiaBan() * item.getSoLuong())
                .sum();

        int count = cartItems.stream().mapToInt(CartItems::getSoLuong).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sản phẩm đã được thêm vào giỏ hàng!");
        response.put("count", count);
        response.put("totalAmount", totalAmount); // Trả về tổng tiền giỏ hàng

        return ResponseEntity.ok(response);
    }

    @RequestMapping("/shopping-cart")
    public String showShoppingCart(Model model, HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Tính tổng tiền
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getGiaBan() * item.getSoLuong())
                .sum();

        // Thêm cartItems và totalAmount vào model để gửi tới view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "views/shoppingCart";  // Trả về tên của view giỏ hàng
    }



    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getCartItemCount(HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        int count = 0;

        if (cartItems != null) {
            count = cartItems.stream().mapToInt(CartItems::getSoLuong).sum();
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }



    // Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        // Log dữ liệu trong session
        System.out.println("Cart Items:");
        for (CartItems item : cartItems) {
            System.out.println("ID: " + item.getSanPhamId() + ", Name: " + item.getTenSanPham() + ", Price: " + item.getGiaBan() + ", Quantity: " + item.getSoLuong());
        }
        model.addAttribute("cartItems", cartItems);
        return "views/shoppingCart"; // Return the view for the cart page
    }
    @PostMapping("/cart/update/{sanPhamId}")
    @ResponseBody
    public ResponseEntity<String> updateItemQuantity(@PathVariable Integer sanPhamId, @RequestParam Integer soLuong, HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Cập nhật số lượng sản phẩm trong giỏ hàng
        for (CartItems item : cartItems) {
            if (item.getSanPhamId().equals(sanPhamId)) {
                item.setSoLuong(soLuong);
                break;

            }

        }

        // Lưu lại giỏ hàng vào session
        session.setAttribute("cartItems", cartItems);

        // Trả về thông báo thành công
        return ResponseEntity.ok("success");
    }

    @GetMapping("/total")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTotalAmount(HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        double totalAmount = 0;

        if (cartItems != null) {
            totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getGiaBan() * item.getSoLuong())
                    .sum();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalAmount", totalAmount);

        return ResponseEntity.ok(response);
    }


    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove/{sanPhamId}")
    public String removeProductFromCart(@PathVariable Integer sanPhamId, HttpSession session) {
        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getSanPhamId().equals(sanPhamId));
            session.setAttribute("cartItems", cartItems);
        }
        return "redirect:/cart"; // Redirect back to the cart page
    }

    // Xóa toàn bộ giỏ hàng
    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cartItems"); // Remove the cart items from session
        return "redirect:/views/cart"; // Redirect to the cart page
    }
    
}
