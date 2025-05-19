import "@typespec/http";
import "@typespec/openapi";

using TypeSpec.Http;
using TypeSpec.OpenAPI;

namespace MineAuth.Models;

/**
 * UUIDモデル
 * プレイヤーの一意識別子
 */
@doc("UUIDモデル")
model UUID {
  @pattern("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
  @example("123e4567-e89b-12d3-a456-426614174000")
  value: string;
}

/**
 * プロフィールデータモデル
 * ユーザープロフィール情報を格納
 */
@doc("プロフィールデータモデル")
model ProfileData {
  /** ユーザー名 */
  @example("Notch")
  username: string;

  /** プレイヤーUUID */
  id: UUID;
}
