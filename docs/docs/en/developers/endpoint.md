# Endpoints
This is an endpoint list for OAuth2.0 and OpenID Connect (OIDC) for Moripa API.

## Endpoints
### OAuth2.0 

- [x] Authorization
  endpoint `/oauth2/authorize` [RFC 6749 4.1.1](https://datatracker.ietf.org/doc/html/rfc6749.html#section-4.1.1)
- [x] Token
  endpoint `/oauth2/token` [RFC 6749 4.1.3](https://datatracker.ietf.org/doc/html/rfc6749.html#section-4.1.3)
- [ ] Introspection
  endpoint `/oauth2/introspect` [RFC 7662](https://datatracker.ietf.org/doc/html/rfc7662.html#section-2.1)
- [ ] Revocation
  endpoint `/oauth2/revoke` [RFC 7009](https://datatracker.ietf.org/doc/html/rfc7009.html#section-2.1)


### OpenID Connect (OIDC)

- [ ] Discovery endpoint `/.well-known/openid-configuration` 
- [x] JWKs endpoint `/.well-known/jwks.json`
- [x] UserInfo
  endpoint `/oauth2/userinfo` [OIDC Core 5.3](https://openid-foundation-japan.github.io/openid-connect-core-1_0.ja.html#UserInfo)

### Plugin

- Plugin endpoint `/api/v1/plugin/<pluginName>/*`