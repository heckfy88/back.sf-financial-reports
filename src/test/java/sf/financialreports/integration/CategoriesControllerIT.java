package sf.financialreports.integration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class CategoriesControllerIT extends AbstractIntegrationClass {


    @DisplayName("Успешное получение категорий")
    @Test
    void getCategories_success() throws Exception {
        getCategoriesSuccess(status().isOk());
    }

    @DisplayName("Неуспешное получение категорий")
    @Test
    void getCategories_fail() throws Exception {
        getCategoriesFail(status().is5xxServerError());
    }

    @DisplayName("Попытка получить категории неавторизованным пользователем")
    @Test
    void getCategories_unauthorized() throws Exception {
        getCategoriesUnauthorized(status().isUnauthorized());
    }

    private String getCategoriesFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions/categories")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getCategoriesSuccess(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/categories")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getCategoriesUnauthorized(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/categories")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }
}
