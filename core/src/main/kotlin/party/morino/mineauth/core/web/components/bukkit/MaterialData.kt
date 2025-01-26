package party.morino.mineauth.core.web.components.bukkit

import kotlinx.serialization.Serializable

@Serializable
data class MaterialData(
    val name: String,
    val type: String
){
    fun toMaterial() = org.bukkit.Material.getMaterial(name)!!
    companion object {
        fun fromMaterial(material: org.bukkit.Material) = MaterialData(material.name, if(material.isBlock) "block" else "item")
    }
}
