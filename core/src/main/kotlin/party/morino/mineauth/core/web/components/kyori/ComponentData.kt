package party.morino.mineauth.core.web.components.kyori

import kotlinx.serialization.Serializable
import party.morino.mineauth.core.utils.ComponentUtils.toMiniMessage

@Serializable
data class ComponentData(
    val text: String,
){
    fun toComponent() = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(text)
    companion object {
        fun fromComponent(component: net.kyori.adventure.text.Component): ComponentData {
            return ComponentData(
                text = component.toMiniMessage()
            )
        }
    }
}
