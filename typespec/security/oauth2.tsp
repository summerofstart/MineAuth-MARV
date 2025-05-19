import "@typespec/http";
import "@typespec/openapi";

using TypeSpec.Http;
using TypeSpec.OpenAPI;

namespace MineAuth.Security;

/**
 * OAuth2認証スキーム
 * 認証コードフロー用の設定
 */
@doc("OAuth2認証")
model OAuth2Schema {
  type: "oauth2";
  flows: {
    authorizationCode: {
      authorizationUrl: "/oauth2/authorize";
      tokenUrl: "/oauth2/token";
      refreshUrl: "/oauth2/token";
      scopes: {
        openid: "Grants read access for OpenID Connect";
        profile: "Grants read access for my profile";
        all: "Grants read access for my resources";
      };
    };
  };
}

/**
 * プロフィールスコープ認証
 * プロフィール情報へのアクセス権を持つ
 */
@doc("プロフィールスコープ認証")
model ProfileAuth {
  scheme: OAuth2Schema;
  scopes: ["profile"];
}

/**
 * 全体スコープ認証
 * すべてのリソースへのアクセス権を持つ
 */
@doc("全体スコープ認証")
model AllAuth {
  scheme: OAuth2Schema;
  scopes: ["all"];
}
