import "@typespec/http";
import "@typespec/openapi";

using TypeSpec.Http;
using TypeSpec.OpenAPI;

namespace MineAuth.Interfaces.PluginIntegrations;

/**
 * QuickShop-Hikari API
 * QuickShop-Hikariプラグイン（ショッププラグイン）との統合API
 */
@route("/api/v1/plugins/quickshop-hikari")
@tag("Quickshop Hikari")
interface QuickShopHikari {
  /**
   * ショップ一覧取得
   * 指定したプレイヤーのショップ一覧を取得
   * @param uuid プレイヤーのUUID。"me"の場合は認証ユーザー自身のショップ
   */
  @route("/users/{uuid}/shops")
  @get
  getShops(@path uuid: string): int64[];
}
