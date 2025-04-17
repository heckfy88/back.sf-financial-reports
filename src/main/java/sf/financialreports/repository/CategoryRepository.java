package sf.financialreports.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.domain.Category;
import sf.financialreports.domain.jooq.enums.CategoryType;

import java.util.List;
import java.util.UUID;

import static sf.financialreports.domain.jooq.Tables.CATEGORY;

@Repository
public class CategoryRepository {

    private final DSLContext dslContext;


    public CategoryRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static List<Field<?>> CATEGORY_FIELDS = List.of(
            CATEGORY.ID,
            CATEGORY.NAME,
            CATEGORY.DESCRIPTION,
            CATEGORY.TYPE,
            CATEGORY.CREATED_AT
    );

    public Category save(Category category) {
        Category existingCategory = findById(category.id());
        if (existingCategory != null) {
            return existingCategory;
        }

        return dslContext
                .insertInto(CATEGORY,
                        CATEGORY.NAME,
                        CATEGORY.DESCRIPTION,
                        CATEGORY.TYPE
                )
                .values(
                        category.name(),
                        category.description(),
                        CategoryType.valueOf(category.type().name())
                )
                .returning(CATEGORY_FIELDS)
                .fetchSingleInto(Category.class);

    }

    public Category findById(UUID id) {
        return dslContext.select(CATEGORY_FIELDS)
                .from(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOneInto(Category.class);
    }

    public boolean isExistingCategory(UUID id) {
        return dslContext.fetchExists(
                dslContext.selectOne()
                        .from(CATEGORY)
                        .where(CATEGORY.ID.eq(id))
        );
    }
}