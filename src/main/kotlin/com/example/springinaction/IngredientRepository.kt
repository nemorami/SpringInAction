package com.example.springinaction


import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.Optional

interface IngredientRepository {
    fun findAll() : List<Ingredient>
    fun findById(id: String) : List<Ingredient>
    fun save(ingredient: Ingredient): Ingredient
}

@Repository
class JdbcIngredientRepository(val jdbcTemplate: JdbcTemplate) : IngredientRepository {



    override fun findAll(): List<Ingredient> {
        return jdbcTemplate.query("select id, name, type from Ingredient", mapper)
    }

    override fun findById(id: String): List<Ingredient> {
        return jdbcTemplate.query("select id, name, type from Ingredient where id=?", mapper,id)

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

