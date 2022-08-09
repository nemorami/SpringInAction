package com.example.springinaction


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.Optional

interface IngredientRepository {
    fun findAll() : List<Ingredient>
    fun findById(id: String) : Ingredient?
    fun save(ingredient: Ingredient): Ingredient
}

@Repository
class JdbcIngredientRepository(@Autowired val jdbcTemplate: JdbcTemplate) : IngredientRepository {
//Check: jdbcTemplate가 @autowired가 아니면?


    override fun findAll(): List<Ingredient> {
        return jdbcTemplate.query("select id, name, type from Ingredient", mapper)
    }

    override fun findById(id: String): Ingredient? {
        val result =  jdbcTemplate.query("select id, name, type from Ingredient where id=?", mapper,id)
        return result.get(0)//return if(result.size != 0)  result.get(0) else null

    }

    override fun save(ingredient: Ingredient): Ingredient {
        jdbcTemplate.update(
            "insert into ingredient (id, name, type) values (?, ?, ?)",
            ingredient.id, ingredient.name, ingredient.type.toString()
        )
        return ingredient
    }

  /*  private fun mapRowToIngredient(row: ResultSet, rowNum: Int): Ingredient{
        return Ingredient(row.getString("id"), row.getString("name"), Ingredient.Type.valueOf(row.getString("type")))
    }*/

    val mapper = RowMapper<Ingredient> { resultSet, rowId ->
        Ingredient(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Ingredient.Type.valueOf(resultSet.getString("type"))
        )
    }
}

