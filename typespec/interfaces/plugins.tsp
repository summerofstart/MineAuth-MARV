import "@typespec/http";
import "@typespec/openapi";

using TypeSpec.Http;
using TypeSpec.OpenAPI;

namespace MineAuth.Interfaces;

/**
 * プラグインAPI
 * プラグイン統合関連のエンドポイント
 */
@route("/api/v1/plugins")
@tag("Plugins")
interface Plugins {
  /**
   * 利用可能プラグイン一覧取得
   * 統合可能なプラグインの一覧を取得
   */
  @route("/availableIntegrations")
  @get
  getAvailableIntegrations(): string[];
}
