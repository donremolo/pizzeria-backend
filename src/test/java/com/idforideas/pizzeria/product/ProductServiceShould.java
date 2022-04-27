package com.idforideas.pizzeria.product;

import static com.idforideas.pizzeria.product.ProductByCategoryObjectMother.PIZZAS;
import static com.idforideas.pizzeria.product.ProductByCategoryObjectMother.getProducts;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ProductServiceShould {

    @Mock
    ProductRepo productRepo;

    @InjectMocks
    ProductServiceImpl productSerice;

    @Test
    @Disabled
    void return_list_products_by_cateogy() {
    

        when(productRepo.findByCategory(PIZZAS)).thenReturn(getProducts());

        assertSame(getProducts(), productSerice.findByCategory(PIZZAS));
    }
    
}
