package com.example.springinaction



import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*

data class Taco(val name: String? = null, val ingredients: List<Ingredient>? = null)

data class Ingredient(val id: String? = null, val name: String? = null, val type: Type? = null) {
    enum class Type(){
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
data class TacoOrder(val deliveryName: String? = null, val deliveryStreet: String? = null, val deliveryCity: String? = null,
                     val deliveryState: String? = null, val deliveryZip: String? = null, val ccNumber: String? = null,
                     val ccExpiration: String? = null, val ccCVV: String? = null) {
    val tacos: MutableList<Taco> = ArrayList()
    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }
}


@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
class DesignTacoController {
    private val log = org.slf4j.LoggerFactory.getLogger(DesignTacoController::class.java)
    @ModelAttribute
    fun addIngredientsToModel(model: Model){
        val ingredients = listOf(
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
        )
        val types = Ingredient.Type.values()
        types.forEach {
            model.addAttribute(it.toString().lowercase(), filterByType(ingredients, it))
        }
    }

    private fun filterByType(ingredients: List<Ingredient>, type: Ingredient.Type): List<Ingredient> {
        return ingredients.filter { (_, _, type1) -> type1 == type}.toList()
    }

    @ModelAttribute(name = "tacoOrder")
    fun order(): TacoOrder {
        return TacoOrder()
    }

    @ModelAttribute(name = "taco")
    fun taco(): Taco {
        return Taco()
    }

    @GetMapping
    fun showDesignForm(): String {
        return "design"
    }

    @PostMapping
    fun processTaco(taco: Taco, @ModelAttribute tacoOrder : TacoOrder): String {
        tacoOrder.addTaco(taco)
        log.info("Processing taco: ${taco}")
        return "redirect:/orders/current"
    }


}
