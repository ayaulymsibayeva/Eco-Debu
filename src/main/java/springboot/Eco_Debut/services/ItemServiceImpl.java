package springboot.Eco_Debut.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.Eco_Debut.entities.*;
import springboot.Eco_Debut.repositories.*;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private TypesRepository typesRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PicturesRepository picturesRepository;

    @Autowired
    private PurchasesRepository purchasesRepository;

    @Autowired
    private CommentsRepository commentsRepository;


    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAllByIdIsNotNullOrderByInTopPageDesc();
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> searchByName(String name) {
        return itemRepository.findAllByNameIsStartingWith(name);
    }

    @Override
    public List<Items> searchByNameAndType(String name, Long id) {
        return itemRepository.findAllByNameIsStartingWithAndType_Id(name,id);
    }

    @Override
    public List<Items> searchByNameAndTypeOrderByPriceAsc(String name,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndType_IdOrderByPriceAsc(name,id);
    }

    @Override
    public List<Items> searchByNameAndTypeOrderByPriceDesc(String name,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndType_IdOrderByPriceDesc(name,id);
    }

    @Override
    public List<Items> searchByNameAndPriceBetweenAndTypeOrderByPriceAsc(String name, double price1, double price2,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenAndType_IdOrderByPriceAsc(name,price1,price2,id);
    }

    @Override
    public List<Items> searchByNameAndPriceBetweenAndTypeOrderByPriceDesc(String name, double price1, double price2,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenAndType_IdOrderByPriceDesc(name,price1,price2,id);
    }

    @Override
    public List<Items> serachByTypeOrderByPriceAsc(Long id) {
        return itemRepository.findAllByType_IdOrderByPriceAsc(id);
    }

    @Override
    public List<Items> serachByTypeOrderByPriceDesc(Long id) {
        return itemRepository.findAllByType_IdOrderByPriceDesc(id);
    }





    @Override
    public List<Items> searchByNameAndCategoryOrderByPriceAsc(String name,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndCategory_IdOrderByPriceAsc(name,id);
    }

    @Override
    public List<Items> searchByNameAndCategoryOrderByPriceDesc(String name,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndCategory_IdOrderByPriceDesc(name,id);
    }

    @Override
    public List<Items> searchByNameAndPriceBetweenAndCategoryOrderByPriceAsc(String name, double price1, double price2,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenAndCategory_IdOrderByPriceAsc(name,price1,price2,id);
    }

    @Override
    public List<Items> searchByNameAndPriceBetweenAndCategoryOrderByPriceDesc(String name, double price1, double price2,Long id) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenAndCategory_IdOrderByPriceDesc(name,price1,price2,id);
    }

    @Override
    public List<Items> serachByCategoryOrderByPriceAsc(Long id) {
        return itemRepository.findAllByCategory_IdOrderByPriceAsc(id);
    }

    @Override
    public List<Items> serachByCategoryOrderByPriceDesc(Long id) {
        return itemRepository.findAllByCategory_IdOrderByPriceDesc(id);
    }





    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoriesRepository.getOne(id);
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoriesRepository.save(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoriesRepository.save(category);
    }


    @Override
    public void deleteCategory (Categories category) {
        categoriesRepository.delete(category);
    }


    @Override
    public List<Types> getAllTypes() {
        return typesRepository.findAll();
    }

    @Override
    public Types getType(Long id) {
        return typesRepository.getOne(id);
    }

    @Override
    public Types addType(Types type) {
        return typesRepository.save(type);
    }

    @Override
    public Types saveType(Types type) {
        return typesRepository.save(type);
    }

    @Override
    public void deleteType(Types type) {
        typesRepository.delete(type);
    }


    @Override
    public List<Sizes> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public Sizes getSize(Long id) {
        return sizeRepository.getOne(id);
    }

    @Override
    public Sizes addSize(Sizes size) {
        return sizeRepository.save(size);
    }

    @Override
    public Sizes saveSize(Sizes size) {
        return sizeRepository.save(size);
    }

    @Override
    public void deleteSize(Sizes size) {
        sizeRepository.delete(size);
    }


    @Override
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles getRole(Long id) {
        return rolesRepository.getOne(id);
    }

    @Override
    public Roles addRole(Roles role) {
        return rolesRepository.save(role);
    }

    @Override
    public Roles saveRole(Roles role) {
        return rolesRepository.save(role);
    }

    @Override
    public void deleteRole(Roles role) {
        rolesRepository.delete(role);
    }


    @Override
    public List<Pictures> getAllPictures() {
        return picturesRepository.findAll();
    }

    @Override
    public Pictures getPicture(Long id) {
        return picturesRepository.getOne(id);
    }

    @Override
    public Pictures addPicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public Pictures savePicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public void deletePicture(Pictures picture) {
            picturesRepository.delete(picture);
    }


    @Override
    public Pictures getPictureByUrl(String url) {
        return picturesRepository.findByUrlEquals(url);
    }

    @Override
    public List<Pictures> findByItem(Long Item_id) {
        return picturesRepository.findAllByItemId(Item_id);
    }


    @Override
    public List<Purchases> getAllPurchases() {
        return purchasesRepository.findAll();
    }

    @Override
    public Purchases getPurchase(Long id) {
        return purchasesRepository.getOne(id);
    }

    @Override
    public Purchases addPurchase(Purchases purchase) {
        return purchasesRepository.save(purchase);
    }

    @Override
    public Purchases savePurchase(Purchases purchase) {
        return purchasesRepository.save(purchase);
    }

    @Override
    public void deletePurchases(Purchases purchase) {
        purchasesRepository.delete(purchase);
    }


    @Override
    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    @Override
    public Comments getComment(Long id) {
        return commentsRepository.getOne(id);
    }

    @Override
    public Comments addComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public Comments saveComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public void deleteComment(Comments comment) {
        commentsRepository.delete(comment);
    }


    @Override
    public List<Comments> itemComments(Long id) {
        return commentsRepository.findAllByItemIdOrderByDateDesc(id);
    }


}
