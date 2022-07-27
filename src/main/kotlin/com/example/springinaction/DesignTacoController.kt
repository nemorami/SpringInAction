package com.example.springinaction



import org.hibernate.validator.constraints.CreditCardNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.*

data class Taco(
    var id: Long? = null,

    @field:NotNull
    @field:Size(min=5, message="Name must be at least 5 characters long")
    var name: String? = null,
    @field:NotNull
    @field:Size(min=1, message="You must choose at least 1 ingredient")
    var ingredients: MutableList<IngredientRef>? = null,
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
    @field:NotBlank(message = "Delivery name is required")
    var deliveryName: String? = null,
    @field:NotBlank(message = "Street is required")
    var deliveryStreet: String? = null,
    @field:NotBlank(message = "City is required")
    var deliveryCity: String? = null,
    @field:NotBlank(message = "State is required")
    var deliveryState: String? = null,
    @field:NotBlank(message = "Zip code is required")
    var deliveryZip: String? = null,
    @field:CreditCardNumber(message = "Not a valid credit card number")
    var ccNumber: String? = null,
    @field:Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
    var ccExpiration: String? = null,
    @field:Digits(integer=3, fraction=0, message="Invalid CVV")
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

        // return "design"
    }

    @PostMapping
    fun processTaco(@ModelAttribute @Valid taco: Taco, errors: Errors, @ModelAttribute tacoOrder : TacoOrder): String {
        log.info("Processing taco: ${taco}")
        if(errors.hasErrors()){
            log.info("Validation Errors ${errors}")
            return "design"
        }
        tacoOrder.addTaco(taco)

        log.info("tocoOrder: ${tacoOrder.tacos}")
        return "redirect:/orders/current"
    }


}
