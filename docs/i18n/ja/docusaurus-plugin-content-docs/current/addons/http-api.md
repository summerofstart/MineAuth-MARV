# HTTP API

MineAuthのHTTP APIについて説明します。

## 概要

MineAuthのHTTP APIは、RESTfulなインターフェースを提供し、MineCraftサーバーとの連携を可能にします。

## API構造

### HTTPメソッド

以下のHTTPメソッドをサポートしています：

- `GET` - リソースの取得
- `POST` - リソースの作成
- `PUT` - リソースの更新
- `DELETE` - リソースの削除
- `PATCH` - リソースの部分更新

### リクエスト

リクエストは以下の形式で送信されます：

```kotlin
data class HttpRequest(
    val parameters: Map<String, String>, // URLパラメータ
    val body: String?,                   // リクエストボディ
    val headers: Map<String, String>     // リクエストヘッダー
)
```

### レスポンス

レスポンスは以下の形式で返却されます：

```kotlin
data class HttpResponse(
    val status: Int,                     // HTTPステータスコード
    val body: String?,                   // レスポンスボディ
    val headers: Map<String, String>     // レスポンスヘッダー
)
```

## アノテーション

APIエンドポイントの定義には以下のアノテーションを使用します：

### HTTPメソッドアノテーション

- `@GetMapping(value: String)` - GETリクエストのエンドポイントを定義
- `@PostMapping(value: String)` - POSTリクエストのエンドポイントを定義
- `@PutMapping(value: String)` - PUTリクエストのエンドポイントを定義
- `@DeleteMapping(value: String)` - DELETEリクエストのエンドポイントを定義

### パラメータアノテーション

- `@RequestParams` - URLパラメータを受け取る
- `@RequestBody` - リクエストボディを受け取る
- `@Params` - 複数のパラメータを一括で受け取る

### 認証関連アノテーション

- `@Authenticated` - 認証が必要なエンドポイントを定義
- `@AuthedAccessUser` - 認証済みユーザー情報を受け取る
- `@Permission` - 必要な権限を定義

## エラーハンドリング

エラーは`HttpError`クラスを使用して処理されます：

```kotlin
class HttpError(
    val status: HttpStatus,              // HTTPステータス
    val message: String,                 // エラーメッセージ
    val details: Map<String, Any>        // エラーの詳細情報
)
```

### 主なHTTPステータスコード

- `200 OK` - リクエスト成功
- `201 Created` - リソース作成成功
- `400 Bad Request` - リクエスト不正
- `401 Unauthorized` - 認証エラー
- `403 Forbidden` - 権限エラー
- `404 Not Found` - リソース未発見
- `500 Internal Server Error` - サーバーエラー

## 使用例

### エンドポイントの定義

```kotlin
@GetMapping("/users")
fun getUsers(): HttpResponse {
    // ユーザー一覧を取得
}

@PostMapping("/users")
fun createUser(@RequestBody user: UserData): HttpResponse {
    // ユーザーを作成
}

@PutMapping("/users/{id}")
fun updateUser(
    @Params(["id"]) params: Map<String, String>,
    @RequestBody user: UserData
): HttpResponse {
    // ユーザーを更新
}

@DeleteMapping("/users/{id}")
fun deleteUser(@Params(["id"]) params: Map<String, String>): HttpResponse {
    // ユーザーを削除
}
``` 