# Config
```json5
{
  "jwt": {
    "issuer": "https://api.example.com/", //if you use proxy, you should set proxy url like "https://api.example.com/lobby" 
    "realm": "example.com",
    "privateKeyFile": "privateKey.pem",
    "keyId": " a22c063-a708-c801-6f92-49f6d53b89b2"
  },
  "oauth" : {
    "applicationName": "MineAuth",
    "logoUrl": "/main/assets/lock.svg"
  },
  "webServer": {
    "port": 8080,
    "ssl": {
      "sslPort": 8443,
      "keyStore": "keystore.jks",
      "keyAlias": "MineAuth",
      "keyStorePassword": "password",
      "privateKeyPassword" : "password"
    } // if you don't need ssl, you can remove this and set null.
  }
}
```