package com.example.springinaction



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.util.*

data class Taco(
    var id: Long? = null,
    val name: String? = null,
    val ingredients: MutableList<IngredientRef>? = null,
    var createAt: Date = Date()
) {
    fun addIngredient(taco: Ingredient) {
        if(taco.id != null)
            ingredients?.add(IngredientRef(taco.id))
    }
}

data class Ingredient(val id: String? = null, val name: String? = null, val type: Type? = null) {
    enum class Type(){
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}

data class IngredientRef(val ingredient: String)
data class TacoOrder(
    var id: Long? = null,
    var deliveryName: String? = null,
    var deliveryStreet: String? = null,
    var deliveryCity: String? = null,
    var deliveryState: String? = null,
    var deliveryZip: String? = null,
    var ccNumber: String? = null,
    var ccExpiration: String? = null,
    var ccCVV: String? = null,
    var placeAt: Date = Date()
) {
    val tacos: MutableList<Taco> = mutableListOf()
    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }
}


@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
class DesignTacoController(@Autowired val ingredientRepo: IngredientRepository) {
    private val log = org.slf4j.LoggerFactory.getLogger(DesignTacoController::class.java)
    @ModelAttribute
    fun addIngredientsToModel(model: Model){
       /* val ingredients = listOf(
            Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
            Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
            Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
            Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
            Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
            Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
            Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
            Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
            Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
            Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE),
        )*/
        val ingredients = ingredientRepo.findAll()
        Ingredient.Type.values().forEach {
            model.addAttribute(it.toString().lowercase(), filterByType(ingredients, it))
        }

    }

    private fun filterByType(ingredients: List<Ingredient>, type: Ingredient.Type): List<Ingredient> {
        return ingredients.filter { it.type == type}
    }

    @ModelAttribute(name = "tacoOrder")
    fun order(): TacoOrder {
        return TacoOrder()
    }

//    @ModelAttribute(name = "taco")
//    fun taco(): Taco {
//        return Taco()
//    }

    @GetMapping
    fun showDesignForm(@ModelAttribute taco: Taco, @ModelAttribute tacoOrder: TacoOrder) {

      //  return "design"
    }

    @PostMapping
    fun processTaco(@ModelAttribute taco: Taco, errors: Errors, @ModelAttribute tacoOrder : TacoOrder): String {
        if(errors.hasErrors()){
            return "design"
        }
        tacoOrder.addTaco(taco)
        log.info("Processing taco: ${taco}")
        log.info("tocoOrder: ${tacoOrder.tacos}")
        return "redirect:/orders/current"
    }


}
