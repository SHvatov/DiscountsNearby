package com.discounts.nearby.model

import com.discounts.nearby.utils.JpaJsonConverter
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

/**
 * Name of the table.
 */
private const val TABLE_NAME = "user"

/**
 * Entity, which stores the information about the supermarket.
 * @author shvatov
 */
@Entity
@Table(name = TABLE_NAME)
class User : AbstractEntity<String>() {
    @Id
    override var id: String? = null

    @get:Column
    var name: String? = null

    @get:Column
    var email: String? = null

    @get:Column(columnDefinition = "JSON")
    @Convert(converter = UserPreferencesJpaConverter::class)
    var preferences: UserPreferences? = null
}

/**
 * Data class, which stores the info about the user preferences in the DB as a basic JSON.
 * @author shvatov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserPreferences(
    @param:JsonProperty(value = "notificationsEnabled")
    val notificationsEnabled: Boolean? = null,

    @param:JsonProperty(value = "searchRadius")
    val searchRadius: BigDecimal? = null,

    @param:JsonProperty(value = "name")
    val favouriteCategories: Set<GoodCategory>? = null
) : Serializable

/**
 * Converts [UserPreferences] into JSON and back.
 * @author shvatov
 */
@Converter(autoApply = true)
class UserPreferencesJpaConverter : JpaJsonConverter<UserPreferences>()