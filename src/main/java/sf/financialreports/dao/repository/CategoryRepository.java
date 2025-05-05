package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.Category;
import sf.financialreports.dao.jooq.enums.CategoryType;

import java.util.List;
import java.util.UUID;

import static sf.financialreports.dao.jooq.Tables.CATEGORY;

@Repository
public class CategoryRepository {

    private final DSLContext dslContext;

    public CategoryRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static List<Field<?>> CATEGORY_FIELDS = List.of(
            CATEGORY.ID,
            CATEGORY.USER_ID,
            CATEGORY.NAME,
            CATEGORY.DESCRIPTION,
            CATEGORY.TYPE,
            CATEGORY.CREATED_AT
    );

    public Category save(Category category) {
        Category existingCategory = findByNameAndUserId(category.getName(), category.getUserId());
        if (existingCategory != null) {
            return existingCategory;
        }

        return dslContext
                .insertInto(CATEGORY,
                        CATEGORY.NAME,
                        CATEGORY.DESCRIPTION,
                        CATEGORY.TYPE,
                        CATEGORY.USER_ID
                )
                .values(
                        category.getName(),
                        category.getDescription(),
                        CategoryType.valueOf(category.getType().name()),
                        category.getUserId()
                )
                .returning(CATEGORY_FIELDS)
                .fetchSingleInto(Category.class);

    }

    public Category findByNameAndUserId(String categoryName, UUID userId) {
        return dslContext.select(CATEGORY_FIELDS)
                .from(CATEGORY)
                .where(CATEGORY.NAME.eq(categoryName))
                .and(CATEGORY.USER_ID.eq(userId))
                .fetchOneInto(Category.class);
    }

    public Category update(Category category) {
        return dslContext.update(CATEGORY)
                .set(CATEGORY.NAME, category.getName())
                .set(CATEGORY.DESCRIPTION, category.getDescription())
                .set(CATEGORY.TYPE, CategoryType.valueOf(category.getType().name()))
                .where(CATEGORY.ID.eq(category.getId()))
                .returning(CATEGORY_FIELDS)
                .fetchSingleInto(Category.class);
    }
}