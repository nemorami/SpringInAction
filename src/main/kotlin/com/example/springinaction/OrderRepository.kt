package com.example.springinaction

import org.springframework.asm.Type
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Types
import java.util.*


interface OrderRepository {
    fun save(order: TacoOrder): TacoOrder
}

@Repository
class JdbcOrderRepository(val jdbcOperations: JdbcOperations) : OrderRepository {
    private val log = org.slf4j.LoggerFactory.getLogger(DesignTacoController::class.java)
    @Transactional
    override fun save(order: TacoOrder): TacoOrder {
        val sql = """insert into Taco_Order
            |(delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip,
            |cc_number, cc_expiration, cc_cvv, placed_at)
            |values (?, ?, ?, ?, ?, ?, ?, ?, ?)          
        """.trimMargin()

//        val pscf = PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
//            ||Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
//            ||Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP)
//        pscf.setReturnGeneratedKeys(true)
//
//        val psc = pscf.newPreparedStatementCreator(
//            listOf(
//                order.deliveryName, order.deliveryStreet, order.deliveryState, order.deliveryZip,
//                order.ccNumber, order.ccExpiration, order.ccCVV, order.placeAt
//            )
//        )

        val psc = PreparedStatementCreatorFactory(
            sql, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        ).apply {
            setReturnGeneratedKeys(true)
        }.newPreparedStatementCreator(
            listOf(
                order.deliveryName, order.deliveryStreet, order.deliveryCity, order.deliveryState, order.deliveryZip,
                order.ccNumber, order.ccExpiration, order.ccCVV, order.placeAt
            )
        )
        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(psc, keyHolder)

        // keyHolder get 이 null을 린터할수 있는지
        //log.info(keyHolder.key.toString())
        order.id = keyHolder.keys?.get("id").toString().toLong()
        order.tacos.forEachIndexed { index, taco ->
            saveTaco(order.id!!, index+1,  taco)
        }
        log.info(order.toString())
        return order
    }

    private fun saveTaco(orderId: Long, orderKey: Int, taco: Taco): Long{
        taco.createAt = Date()
        val sql = """
            insert into Taco(name, created_at, taco_order, taco_order_key)
            values (?,?,?,?)
        """.trimIndent()
        val psc = PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG).apply {
            setReturnGeneratedKeys(true)
        }.newPreparedStatementCreator(listOf(taco.name, taco.createAt, orderId, orderKey))

        val keyHolder = GeneratedKeyHolder()
        //jdbcOperations.update(psc, keyHolder)
        jdbcOperations.update(psc, keyHolder,);


        val tacoId = keyHolder.keys?.get("id").toString().toLong()
        taco.id = tacoId
        saveIngredientRefs(tacoId, taco.ingredients)
        return tacoId

    }

    private fun saveIngredientRefs(tacoId: Long, ingredients: MutableList<IngredientRef>?) {
        ingredients?.forEachIndexed { index, ingredient ->
            jdbcOperations.update("""
                insert into ingredient_Ref (ingredient, taco, taco_key)
                values (?,?,?)
            """.trimIndent(),
            ingredient, tacoId, index+1)
        }

    }


}