package com.discounts.nearby.model

import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.utils.JpaJsonConverter
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

/**
 * Name of the table.
 */
private const val TABLE_NAME = "supermarket"

/**
 * Entity, which stores the information about the supermarket.
 * @author shvatov
 */
@Entity
@Table(name = TABLE_NAME)
class Supermarket : AbstractEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override var id: Long? = null

    @get:Column
    var name: String? = null

    @get:Column
    @get:Enumerated(EnumType.STRING)
    var code: SupermarketCode? = null

    @get:Column(columnDefinition = "CLOB(10K)", name = "GOODS_SORTED_BY_PRICE")
    @get:Convert(converter = GoodsJpaConverter::class)
    @Lob
    var goodsSortedByPrice: Goods? = null

    @get:Column(columnDefinition = "CLOB(10K)", name = "GOODS_SORTED_BY_DISCOUNT")
    @get:Convert(converter = GoodsJpaConverter::class)
    @Lob
    var goodsSortedByDiscount: Goods? = null
}

/**
 * Wrapper for list of [Good] in order to store them in DB as one JSON.
 * @author shvatov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Goods @JsonCreator constructor(
    @get:JsonProperty(value = "goods")
    val goods: List<Good>? = null
) : Serializable

/**
 * Data class, which stores the info about the good in the DB as a basic JSON.
 * @author shvatov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Good @JsonCreator constructor(
    @param:JsonProperty(value = "name")
    var name: String? = null,

    @param:JsonProperty(value = "pathToPicture")
    var pathToPicture: String? = null,

    @param:JsonProperty(value = "goodCategory")
    var goodCategory: GoodCategory? = null,

    @param:JsonProperty(value = "weight")
    var weight: String? = null,

    @param:JsonProperty(value = "discount")
    var discount: BigDecimal? = null,

    @param:JsonProperty(value = "price")
    var price: BigDecimal? = null
) : Serializable

/**
 * Converts [Goods] into JSON and back.
 * @author shvatov
 */
@Converter(autoApply = true)
class GoodsJpaConverter : JpaJsonConverter<Goods>()