

import com.example.springinaction.Ingredient
import com.example.springinaction.IngredientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IngredientByIdConverter @Autowired constructor(private val ingredientRepo: IngredientRepository) :
    Converter<String?, Ingredient?> {
    override fun convert(id: String): Ingredient? {
        return ingredientRepo.findById(id)
    }
}