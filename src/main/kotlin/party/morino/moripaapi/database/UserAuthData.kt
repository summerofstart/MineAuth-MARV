package party.morino.moripaapi.database

import org.jetbrains.exposed.sql.Table

object UserAuthData: Table() {
    val uuid = varchar("uuid", 40)
    val password = varchar("password", 255)
    override val primaryKey = PrimaryKey(uuid)
}