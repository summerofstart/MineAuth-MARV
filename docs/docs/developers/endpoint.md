# Endpoints
This is an endpoint list for OAuth2.0 and OpenID Connect (OIDC) for Moripa API.

## Endpoints
### OAuth2.0 

- [x] Authorization
  endpoint `/oauth2/authorize` [RFC 6749 4.1.1](https://openid-foundation-japan.github.io/rfc6749.ja.html#code-authz-req)
- [x] Token
  endpoint `/oauth2/token` [RFC 6749 4.1.3](https://openid-foundation-japan.github.io/rfc6749.ja.html#token-endpoint)


### OpenID Connect (OIDC)

- [ ] Discovery endpoint `/.well-known/openid-configuration` 
- [x] JWKs endpoint `/.well-known/jwks.json`

### Plugin

- Plugin endpoint `/api/v1/plugin/<pluginName>/*`