package com.idforideas.pizzeria.docs.category;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.hamcrest.Matchers.containsString;

import com.idforideas.pizzeria.docs.support.MockBase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class CategoryResourceTest extends MockBase {

    @Test
    void getCategory() throws Exception {
        mockMvc.perform(get("/api/v1/categories/category/pizzas").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(document("{class-name}/{method-name}", 
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                relaxedResponseFields(
                    subsectionWithPath("data.category").description("Información de la categoría recuperada"),
                    subsectionWithPath("data.category.name").description("Nombre de la categoría"))
            ));
    }
    
    @Test
    void getAllCategories() throws Exception {
        mockMvc.perform(get("/api/v1/categories").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(document("{class-name}/{method-name}", 
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                relaxedResponseFields(
                    subsectionWithPath("data.categories").description("Contiene toda la información de paginación"),
                    subsectionWithPath("data.categories.content").description("Información de categorías de la pagina actual"))
            ));
    }

    @Test
    void addCategory() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
        .with(userToken())
        .contentType(APPLICATION_JSON)
        .content("{\"name\":\"Helados\"}"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("/api/v1/categories/5")))
        .andExpect(content().contentType(APPLICATION_JSON))
        .andDo(document("{class-name}/{method-name}", 
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            relaxedResponseFields(
                subsectionWithPath("data.category").description("Información de categoría creada"),
                subsectionWithPath("data.category.name").description("Nombre de la categoría"))
            ));
    }

    @Test
    void deleteCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/2")
            .contentType(APPLICATION_JSON)
            .with(userToken()))
            .andExpect(status().isNoContent());
    }
}
