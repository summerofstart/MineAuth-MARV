package party.morino.mineauth.core.web.components.bukkit

import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import party.morino.mineauth.core.web.components.kyori.ComponentData

@Serializable
data class ItemStackData(
    val type: MaterialData,
    val amount: Int,
    val lore : List<ComponentData>,
    val meta : ItemMetaData
){
    fun toItemStack(): org.bukkit.inventory.ItemStack {
        val itemStack = org.bukkit.inventory.ItemStack(type.toMaterial(), amount)
        itemStack.itemMeta = meta.toItemMeta(type.toMaterial())
        itemStack.lore(lore.map { it.toComponent() })
        return itemStack
    }

    companion object {
        fun fromItemStack(itemStack: org.bukkit.inventory.ItemStack): ItemStackData {
            return ItemStackData(
                type = MaterialData.fromMaterial(itemStack.type),
                amount = itemStack.amount,
                lore = itemStack.lore()?.map { ComponentData.fromComponent(it) } ?: emptyList(),
                meta = ItemMetaData.fromItemMeta(itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type))
            )
        }
    }
}
