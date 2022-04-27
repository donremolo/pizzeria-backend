package com.idforideas.pizzeria.docs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.idforideas.pizzeria.docs.support.MockBase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DocumentationTest extends MockBase {

    @Test
    @Disabled
    void docsForwarding() throws Exception {
        mockMvc.perform(get("/docs"))
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/docs/index"));
    }
}
