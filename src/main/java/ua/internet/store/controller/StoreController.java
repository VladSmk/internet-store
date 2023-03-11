package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.StoreDAO;
import ua.internet.store.model.Product;

@Controller
@RequestMapping("/store")
public class StoreController {
    private final StoreDAO storeDAO;
    @Autowired
    public StoreController(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    @GetMapping("/test")
    public String testMethod(){
        return "test";
    }


    @GetMapping()
    public String storeFirstPage(Model model){
        model.addAttribute("allProductsList", storeDAO.findAllProduct());
        model.addAttribute("firstColumn", storeDAO.arrayOfThreeList()[0]);
        model.addAttribute("secondColumn", storeDAO.arrayOfThreeList()[1]);
        model.addAttribute("thirdColumn", storeDAO.arrayOfThreeList()[2]);
        return "store/store";
    }

    @GetMapping("/{userId}")
    public String storePageForSomeUser(@PathVariable("userId") int userId, Model model){
        model.addAttribute("wantedUser", storeDAO.searchUserInDbById(userId));
        model.addAttribute("allProductsList", storeDAO.findAllProduct());
        model.addAttribute("idUser", userId);
        model.addAttribute("firstColumn", storeDAO.arrayOfThreeList()[0]);
        model.addAttribute("secondColumn", storeDAO.arrayOfThreeList()[1]);
        model.addAttribute("thirdColumn", storeDAO.arrayOfThreeList()[2]);
        return "store/store-USERID";
    }


    @GetMapping("/lookItem/{itemId}")
    public String storeLookItem(@PathVariable("itemId") int itemId, Model model){
        model.addAttribute("objProduct", storeDAO.searchProductById(itemId));
        return "store/store-lookitem-ITEMID";
    }

    @GetMapping("/{userId}/lookItem/{itemId}")
    public String storeUserLookItem(@PathVariable("itemId") int itemId,
                                    @PathVariable("userId") int userid,
                                    Model model){
        model.addAttribute("objProduct", storeDAO.searchProductById(itemId));
        model.addAttribute("objInBasket", storeDAO.checkProductInBasket(userid, itemId));
        model.addAttribute("idUser", userid);
        return "store/store-USERID-lookItem-ITEMID";
    }
    @GetMapping("/myItem/{userId}")
    public String PageWithUserItemsForSale(@PathVariable("userId") int userId, Model model){
        model.addAttribute("productList", storeDAO.listProductByAuthorId(userId));
        model.addAttribute("userId", userId);
        return "store/store-myitem-USERID";
    }

    @GetMapping("/{userId}/createItem")
    public String storeCreateItem(@PathVariable("userId") int userId, Model model){
        model.addAttribute("newProduct", new Product());
        model.addAttribute("idUser", userId);
        return "store/store-createitem-USERID";
    }

    @PostMapping("/postCreateProduct/{userId}")
    public String postStoreCreateItem(@PathVariable("userId") int userId,
                                      @ModelAttribute("newProduct") Product product){
        System.out.println("id=" + userId + " name=" + storeDAO.searchUserNameById(userId));
        product.setAuthor_id(String.valueOf(userId));
        storeDAO.saveProductInDb(product);
        return "redirect:/store/"+userId;
    }

    @GetMapping("/chi/{itemId}")
    public String changeItemsForSaleByItemId(@PathVariable("itemId") int itemId, Model model){
        System.out.println("Author_id:"+storeDAO.searchProductById(itemId).getAuthor_id());
        System.out.println("Id:"+storeDAO.searchProductById(itemId).getId());
        System.out.println("Color_id:"+storeDAO.searchProductById(itemId).getColor_id());

        model.addAttribute("wantedProduct", storeDAO.searchProductById(itemId));
        model.addAttribute("itemId", itemId);
        return "store/store-chi-ITEMID";
    }

    @PatchMapping("/patchEditProduct/{itemId}")
    public String patchMapEditProduct(@PathVariable("itemId") int itemId,
                                      @ModelAttribute("wantedProduct") Product product){
        product.setId(itemId);
        product.setAuthor_id(String.valueOf(storeDAO.getProductAuthorIdByProductId(itemId)));

        System.out.println("Patch Author_id:"+product.getAuthor_id());
        System.out.println("Patch Id:"+product.getId());
        System.out.println("Patch Color_id:"+product.getColor_id());

        storeDAO.editProductInDb(product);
        return "redirect:/store/"+product.getAuthor_id();
    }




}
