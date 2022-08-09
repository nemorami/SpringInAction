package com.example.springinaction
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    /**
     * Configure simple automated controllers pre-configured with the response
     * status code and/or a view to render the response body. This is useful in
     * cases where there is no need for custom controller logic -- e.g. render a
     * home page, perform simple site URL redirects, return a 404 status with
     * HTML content, a 204 with no content, and more.
     * @see ViewControllerRegistry
     */
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("home")
    }
}

//
//data class User(var id:Int?=null, var name: String?=null, var addr: String?=null)
//@Controller
//class HomeController {
//    private val log = org.slf4j.LoggerFactory.getLogger(DesignTacoController::class.java)
//    @GetMapping("/")
//
//    fun home(@ModelAttribute("data") data: User): String {
//        //model.addAttribute("data", User())
//        //val user = User(1, "유상선", "대전")
//        //model.addAttribute("myName", user)
////        model.addAttribute("idName", "myid")
////        model.addAttribute("btnName", "btnId")
////        model.addAttribute("list", listOf(1,2,3,4,5))
//        return "home"
//    }
//    @PostMapping("/")
//    fun result(@ModelAttribute("data") data: User): String{
//        //model.addAttribute("data", data)
//        log.info("user => ${data}")
//        return "result"
//    }
//}