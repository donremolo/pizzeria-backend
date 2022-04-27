package com.idforideas.pizzeria.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

import java.util.List;

import com.idforideas.pizzeria.category.Category;
import com.idforideas.pizzeria.category.CategoryRepo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ProductRepoIT {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Test
    @Disabled
    void return_list_of_product_by_category_ignore_case() {

        Category pizzasIgnoreCase = categoryRepo.findByNameIgnoreCase("pIzZas").get();

        List<Product> actual = productRepo.findByCategory(pizzasIgnoreCase);

        assertEquals("Pizzas", pizzasIgnoreCase.getName());
        assertFalse(actual.isEmpty());
        assertFalse(actual.stream().anyMatch(p -> !"pizzas".equalsIgnoreCase(p.getCategory().getName())));
    }
}
