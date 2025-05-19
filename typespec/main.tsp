import "@typespec/http";
import "@typespec/rest";
import "@typespec/openapi";

// モデル定義のインポート
import "./models/common.tsp";
import "./models/plugins.tsp";

// セキュリティ定義のインポート
import "./security/oauth2.tsp";

// インターフェース定義のインポート
import "./interfaces/oauth2.tsp";
import "./interfaces/server.tsp";
import "./interfaces/plugins.tsp";
import "./interfaces/plugins/vault.tsp";
import "./interfaces/plugins/quickshop-hikari.tsp";

using TypeSpec.Http;
using TypeSpec.Rest;
using TypeSpec.OpenAPI;
using MineAuth.Security;

/**
 * MineAuth API
 * Minecraft用認証プラグインのAPI仕様
 */
@doc("This is the MineAuth API documentation.\nThis makes user access to self information on minecraft.")
@service(#{ title: "MineAuth" })
@info(#{
  version: "1.0.0",
  license: #{
    name: "CC0-1.0",
    url: "https://creativecommons.org/publicdomain/zero/1.0/",
  },
})
@server("http://localhost:8080", "Local server")
@server("https://api.example.com", "Production server")
namespace MineAuth {

}
