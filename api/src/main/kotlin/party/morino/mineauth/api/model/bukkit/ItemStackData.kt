package party.morino.mineauth.api.model.bukkit

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import party.morino.mineauth.api.model.kyori.ComponentData

@Serializable
data class ItemStackData(
    val type: MaterialData,
    val amount: Int,
    val lore : List<ComponentData>,
    val meta : ItemMetaData
){
    fun toItemStack(): ItemStack {
        val itemStack = ItemStack(type.toMaterial(), amount)
        itemStack.itemMeta = meta.toItemMeta(type.toMaterial())
        itemStack.lore(lore.map { it.toComponent() })
        return itemStack
    }

    companion object {
        fun fromItemStack(itemStack: ItemStack): ItemStackData {
            return ItemStackData(
                type = MaterialData.fromMaterial(itemStack.type),
                amount = itemStack.amount,
                lore = itemStack.lore()?.map { ComponentData.Companion.fromComponent(it) } ?: emptyList(),
                meta = ItemMetaData.fromItemMeta(itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type))
            )
        }
    }
}