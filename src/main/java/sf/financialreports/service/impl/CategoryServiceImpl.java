package sf.financialreports.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.repository.CategoryRepository;
import sf.financialreports.dao.repository.UserRepository;
import sf.financialreports.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;

    @Override
    public List<CategoryDto> getCategories() {
        User user = getUserFromToken();
        return categoryRepository.getCategories(user.getId()).stream().map(category ->
                CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .type(category.getType())
                        .build()
        ).toList();
    }

    private User getUserFromToken() {
        Map<String, Object> claims = authenticationService.getToken().getClaims();
        return userRepository.findById(UUID.fromString(claims.get("sub").toString()));
    }
}
