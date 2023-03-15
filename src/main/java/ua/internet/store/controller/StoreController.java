package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.StoreDAO;
import ua.internet.store.model.LeftFilter;
import ua.internet.store.model.Product;
import ua.internet.store.model.UpperFilter;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/store")
public class StoreController {
    private final StoreDAO storeDAO;
    @Autowired
    public StoreController(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }
    private static ArrayList<Product> arrayListToStore = new ArrayList<Product>();
    private static UpperFilter staticUpperFilter = new UpperFilter("-", "-", "-", "-","-","-");
    private static boolean oneTimeUsage = true;
//    @GetMapping("/test")
//    public String testMethod(Model model){
//        model.addAttribute("listWithColor", storeDAO.getListWithColor());
//        model.addAttribute("listWithFirm", storeDAO.getListWithFirm());
//        model.addAttribute("objLeftFilter", new LeftFilter());
//        return "static/images/test";
//    }
//    @PostMapping("/test/")
//    public void processForm(@RequestParam("colorArray") String[] websites) {
//        for (String website : websites) {
//            System.out.println(website);
//        }
//    }
//    private String[] getWebsites() {
//        return new String[]{"Google", "Facebook", "Twitter", "LinkedIn"};
//    }
    @GetMapping()
    public String storeFirstPage(Model model){
        model.addAttribute("listWithCountries", storeDAO.getAllNamesFromTable(
                "countries",
                "country"
        ));
        model.addAttribute("listWithCities", storeDAO.getAllNamesFromTable(
                "cities",
                "city"
        ));
        model.addAttribute("listWithColors", storeDAO.getAllNamesFromTable(
                "colors",
                "color"
        ));
        model.addAttribute("listWithTypes", storeDAO.getAllNamesFromTable(
                "types",
                "type"
        ));
        model.addAttribute("listWithFirms", storeDAO.getAllNamesFromTable(
                "firms",
                "firm"
        ));
        model.addAttribute("listWithAuthors", storeDAO.getAllNamesFromTable(
                "users",
                "username"
        ));
        if(oneTimeUsage) {
            arrayListToStore = storeDAO.findAllProduct();
            oneTimeUsage=false;
        }


        model.addAttribute("firstColumn", storeDAO.arrayOfThreeList(arrayListToStore)[0]);
        model.addAttribute("secondColumn", storeDAO.arrayOfThreeList(arrayListToStore)[1]);
        model.addAttribute("thirdColumn", storeDAO.arrayOfThreeList(arrayListToStore)[2]);
        model.addAttribute("upperFilter", staticUpperFilter);
        return "store/store";
    }

    @PostMapping("/postUpperFilter")
    public String postProductAfterUpperFilter(@ModelAttribute("storeInput") UpperFilter upperFilter){
        staticUpperFilter=upperFilter;
        arrayListToStore=storeDAO.getProductsAfterFiltering(staticUpperFilter);
        return "redirect:/store";
    }

    @PostMapping("/postSearchBar")
    public String postProductSearchBar(@RequestParam("partOfWord") String partOfWord){
//        System.out.println("|" + partOfWord + "|");
        if(partOfWord=="")
            arrayListToStore=storeDAO.findAllProduct();
        else
            arrayListToStore=storeDAO.listProductByPartOfAuthorName(partOfWord);
        return "redirect:/store";
    }

//    @PostMapping("/postLeftFilter")
//    public String postProductAfterLeftFilter(@RequestParam("colorArray") String[] stringsWithColor,
//                       @RequestParam("firmArray") String[] stringsWithFirm){
//        System.out.println("Color: ");
//        for(String str : stringsWithColor)
//            System.out.println(str);
//        System.out.println("Firm: ");
//        for(String str : stringsWithFirm)
//            System.out.println(str);
//        return "redirect:/store";
//    }


    @GetMapping("/{userId}")
    public String storePageForSomeUser(@PathVariable("userId") int userId, Model model){
        model.addAttribute("wantedUser", storeDAO.searchUserInDbById(userId));
        model.addAttribute("allProductsList", storeDAO.findAllProduct());
        model.addAttribute("idUser", userId);
        model.addAttribute("firstColumn", storeDAO.arrayOfThreeList(arrayListToStore)[0]);
        model.addAttribute("secondColumn", storeDAO.arrayOfThreeList(arrayListToStore)[1]);
        model.addAttribute("thirdColumn", storeDAO.arrayOfThreeList(arrayListToStore)[2]);
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
        product.setAuthor_id(String.valueOf(userId));
        storeDAO.saveProductInDb(product);
        return "redirect:/store/"+userId;
    }
    @GetMapping("/chi/{itemId}")
    public String changeItemsForSaleByItemId(@PathVariable("itemId") int itemId, Model model){

        model.addAttribute("wantedProduct", storeDAO.searchProductById(itemId));
        model.addAttribute("itemId", itemId);
        return "store/store-chi-ITEMID";
    }
    @PatchMapping("/patchEditProduct/{itemId}")
    public String patchMapEditProduct(@PathVariable("itemId") int itemId,
                                      @ModelAttribute("wantedProduct") Product product){
        product.setId(itemId);
        product.setAuthor_id(String.valueOf(storeDAO.getProductAuthorIdByProductId(itemId)));

        storeDAO.editProductInDb(product);
        return "redirect:/store/"+product.getAuthor_id();
    }
    @DeleteMapping("/deleteMyItem/{itemId}")
    public String deleteMyItem(@PathVariable("itemId") int itemId){
        int authorId = storeDAO.getProductAuthorIdByProductId(itemId);
        storeDAO.deleteMyItemFromDb(itemId);
        return "redirect:/store/myItem/"+authorId;
    }
}
