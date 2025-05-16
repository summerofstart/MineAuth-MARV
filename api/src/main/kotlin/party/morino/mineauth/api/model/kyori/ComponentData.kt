package party.morino.mineauth.api.model.kyori

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

@Serializable
data class ComponentData(
    val text: String,
){
    fun toComponent() = MiniMessage.miniMessage().deserialize(text)
    companion object {
        fun fromComponent(component: Component): ComponentData {
            return ComponentData(
                text = MiniMessage.miniMessage().serialize(component)
            )
        }
    }
}
