# Endpoints
This is an endpoint list for OAuth2.0 and OpenID Connect (OIDC) for Moripa API.

## Endpoints
### OAuth2.0 

- [ ] Authorization
  endpoint `/oauth2/authorize` [RFC 6749 4.1.1](https://openid-foundation-japan.github.io/rfc6749.ja.html#code-authz-req)
  scopeの区切り文字としてはスペースを使用 [RFC 6749 3.3](https://datatracker.ietf.org/doc/html/rfc6749#section-3.3)
- [ ] Token
  endpoint `/oauth2/token` [RFC 6749 4.1.3](https://openid-foundation-japan.github.io/rfc6749.ja.html#token-endpoint)
- [] Register endpoint


### OpenID Connect (OIDC)

- [ ] Discovery endpoint `/.well-known/openid-configuration` 
- [x] JWKs endpoint `/.well-known/jwks.json`

### Plugin

- Plugin endpoint `/api/v1/plugin/<pluginName>/*`