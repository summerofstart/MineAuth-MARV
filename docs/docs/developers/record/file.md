# File base record data reference

## File structure

```
└── Moripa-API/
    ├── certificate.pem
    ├── jwks.json
    ├── Moripa-API.db
    ├── privateKey.pem
    ├── publicKey.pem
    │
    ├─assets/
    │   └── lock.svg
    ├─clients/
    │   └── 3a10ca7a-6014-4b42-b418-b6847a490e14/
    │       └── data.json
    ├─config/
    │   ├── oauth.json
    │   └── web-server.json
    ├─load/
    │   ├── jwt.json
    │   ├── oauth.json
    │   └── web-server.json
    └─templates/
        └── authorize.vm
```

## File list

`plugin-dir/clients/<clientId>/data.json`

```json
{
  "clientId": "3a10ca7a-6014-4b42-b418-b6847a490e14",
  "clientName": "debug",
  "redirectUri": "https://oauthdebugger.com/debug"
}
```

`plugin-dir/assets` To copy from resources/assets and this is route to access assets <br>
`plugin-dir/templates` To copy from resources/templates