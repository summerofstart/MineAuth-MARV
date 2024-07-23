package party.morino.mineauth.core.web.components.bukkit

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
){
    fun toLocation() = org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch)
    companion object {
        fun fromLocation(location: org.bukkit.Location) = LocationData(location.world.name, location.x, location.y, location.z, location.yaw, location.pitch)
    }
}