package party.morino.mineauth.api

import org.bukkit.plugin.java.JavaPlugin

/**
 * MineAuthプラグインのAPIインターフェース
 * 他のプラグインがMineAuthと連携するためのエントリーポイントとなります
 */
interface MineAuthAPI {
    /**
     * プラグイン用のハンドラーを作成します
     *
     * @param plugin ハンドラーを作成するプラグインのインスタンス
     * @return 作成されたRegisterHandler
     */
    abstract fun createHandler(plugin : JavaPlugin): RegisterHandler
}