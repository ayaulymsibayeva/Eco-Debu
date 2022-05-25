package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Items;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items,Long> {
    List<Items> findAllByIdIsNotNullOrderByInTopPageDesc();
    List<Items> findAllByNameIsStartingWith(String name);
    List<Items> findAllByType_IdOrderByPriceAsc(Long id);
    List<Items> findAllByType_IdOrderByPriceDesc(Long id);
    List<Items> findAllByNameIsStartingWithAndType_Id(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndType_IdOrderByPriceAsc(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndType_IdOrderByPriceDesc(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenAndType_IdOrderByPriceAsc(String name,double price1,double price2,Long id);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenAndType_IdOrderByPriceDesc(String name,double price1,double price2,Long id);


    List<Items> findAllByCategory_IdOrderByPriceAsc(Long id);
    List<Items> findAllByCategory_IdOrderByPriceDesc(Long id);
    List<Items> findAllByNameIsStartingWithAndCategory_Id(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndCategory_IdOrderByPriceAsc(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndCategory_IdOrderByPriceDesc(String name,Long id);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenAndCategory_IdOrderByPriceAsc(String name,double price1,double price2,Long id);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenAndCategory_IdOrderByPriceDesc(String name,double price1,double price2,Long id);


}
