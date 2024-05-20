package party.morino.mineauth.core.web.router.common.data

import kotlinx.serialization.Serializable
import party.morino.mineauth.core.utils.UUIDSerializer
import java.util.*

@Serializable
data class ProfileData(
    val username: String, val id: @Serializable(with = UUIDSerializer::class) UUID
)