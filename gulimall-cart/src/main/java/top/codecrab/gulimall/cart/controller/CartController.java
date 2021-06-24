package top.codecrab.gulimall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.codecrab.gulimall.cart.interceptor.CartInterceptor;
import top.codecrab.gulimall.cart.service.CartService;
import top.codecrab.gulimall.cart.to.UserInfoTo;
import top.codecrab.gulimall.cart.vo.CartItemVo;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author codecrab
 * @since 2021年06月24日 16:56
 */
@Controller
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("/cart.html")
    public String cartListPage() {
        UserInfoTo infoTo = CartInterceptor.THREAD_LOCAL.get();

        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(
            @RequestParam Long skuId,
            @RequestParam Integer num,
            RedirectAttributes attributes
    ) throws ExecutionException, InterruptedException {
        cartService.getCartItemBySkuId(skuId, num);

        //添加重定向参数，给成功请求发送消息，避免页面重复刷新造成多次添加购物车
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccess(@RequestParam Long skuId, Model model) {
        CartItemVo cartItemVo = cartService.getCartItemByRedis(skuId);
        model.addAttribute("item", cartItemVo);
        return "success";
    }

}
