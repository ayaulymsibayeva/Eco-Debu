package springboot.Eco_Debut.services;

import springboot.Eco_Debut.entities.*;

import java.util.List;

public interface ItemService {
    Items addItem(Items item);
    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);
    List<Items> searchByName(String name);
    List<Items> searchByNameAndType(String name,Long id);
    List<Items> searchByNameAndTypeOrderByPriceAsc(String name,Long id);
    List<Items> searchByNameAndTypeOrderByPriceDesc(String name,Long id);
    List<Items> serachByTypeOrderByPriceAsc(Long id);
    List<Items> serachByTypeOrderByPriceDesc(Long id);

    List<Items> searchByNameAndPriceBetweenAndTypeOrderByPriceAsc(String name,double price1,double price2,Long id);
    List<Items> searchByNameAndPriceBetweenAndTypeOrderByPriceDesc(String name,double price1,double price2,Long id);


    List<Items> searchByNameAndCategoryOrderByPriceAsc(String name,Long id);
    List<Items> searchByNameAndCategoryOrderByPriceDesc(String name,Long id);
    List<Items> serachByCategoryOrderByPriceAsc(Long id);
    List<Items> serachByCategoryOrderByPriceDesc(Long id);

    List<Items> searchByNameAndPriceBetweenAndCategoryOrderByPriceAsc(String name,double price1,double price2,Long id);
    List<Items> searchByNameAndPriceBetweenAndCategoryOrderByPriceDesc(String name,double price1,double price2,Long id);



    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    Categories addCategory(Categories category);
    Categories saveCategory(Categories category);
    void deleteCategory(Categories category);


    List<Types> getAllTypes();
    Types getType(Long id);
    Types addType(Types type);
    Types saveType(Types type);
    void  deleteType(Types type);



    List<Sizes> getAllSizes();
    Sizes getSize(Long id);
    Sizes addSize(Sizes size);
    Sizes saveSize(Sizes size);
    void deleteSize(Sizes size);



    List<Roles> getAllRoles();
    Roles getRole(Long id);
    Roles addRole(Roles role);
    Roles saveRole(Roles role);
    void deleteRole(Roles role);


    List<Pictures> getAllPictures();
    List<Pictures> findByItem(Long Item_id);
    Pictures getPicture(Long id);
    Pictures addPicture(Pictures picture);
    Pictures savePicture(Pictures picture);
    void deletePicture(Pictures picture);
    Pictures getPictureByUrl(String url);



    List<Purchases> getAllPurchases();
    Purchases getPurchase(Long id);
    Purchases addPurchase(Purchases purchase);
    Purchases savePurchase(Purchases purchase);
    void deletePurchases(Purchases purchase);


    List<Comments> getAllComments();
    List<Comments> itemComments(Long id);
    Comments getComment(Long id);
    Comments addComment(Comments comment);
    Comments saveComment(Comments comment);
    void deleteComment(Comments comment);


    List<Cities> getAllCities();
    Cities getCity(Long id);
    Cities saveCity(Cities city);
    void  deleteCity(Cities city);


}
