# Config
## JWT
file path: plugins/MineAuth/load/jwt.json
This config is used to generate JWT token.

This is automatically generated when the server starts, so please set the issue and the realm
```json5
{
    "issuer": "https://api.example.com/", //if you use proxy, you should set proxy url like "https://api.example.com/lobby" 
    "realm": "example.com",
    "privateKeyFile": "privateKey.pem",
    "keyId": " a22c063-a708-c801-6f92-49f6d53b89b2"
}
```

## OAuth2
file path: plugins/MineAuth/load/oauth.json

This is automatically generated when the server starts, so if you want to change login page, edit it.
```json5
{
  "applicationName": "MineAuth",
  "logoUrl": "/main/assets/lock.svg"
}
```

## WebServer
file path: plugins/MineAuth/load/web-server.json

This is automatically generated when the server starts, so if you want to change port, edit it.
```json5
{
  "port": 8080,
  "ssl": {
    "sslPort": 8443,
    "keyStore": "keystore.jks",
    "keyAlias": "MineAuth",
    "keyStorePassword": "password",
    "privateKeyPassword" : "password"
  }, // if you don't need ssl, you can remove this and set null.
}
```