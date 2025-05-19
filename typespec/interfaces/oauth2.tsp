import "@typespec/http";
import "@typespec/openapi";
import "../models/common.tsp";
import "../security/oauth2.tsp";

using TypeSpec.Http;
using TypeSpec.OpenAPI;
using MineAuth.Models;
using MineAuth.Security;
using MineAuth.Security.SecuritySchemes;

namespace MineAuth.Interfaces;

/**
 * OAuth2 API
 * 認証関連のエンドポイント
 */
@route("/oauth2")
@tag("oauth2")
interface OAuth2 {
  /**
   * ユーザー情報取得
   * 認証されたユーザーのプロフィール情報を取得
   */
  @route("/userinfo")
  @get
  @useAuth(OAuth2)
  getUserInfo(): ProfileData;

  /**
   * アクセストークン無効化
   * 指定されたトークンを無効化する
   */
  @route("/revoke")
  @post
  revokeToken(@query token: string): void;

  /**
   * アクセストークン検証
   * 指定されたトークンの情報を検証する
   */
  @route("/introspect")
  @post
  introspectToken(@query token: string): void;
}
