package party.morino.moripaapi.web.router.common.data

import kotlinx.serialization.Serializable
import party.morino.moripaapi.utils.UUIDSerializer
import java.util.*

@Serializable
data class ProfileData(
    val username: String, val uuid: @Serializable(with = UUIDSerializer::class) UUID
)