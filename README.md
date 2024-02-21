# Moripa-API

This is a plugin for accessing all moripa from the web. 

## Attention for developers
It is hard to create an API and register it for each plugin, so use softdepend and implement it in hardcourt.
Also, when converting from normal class to json, if you make your own encoder, it will be buggy when outputting in openAPI format, so use the data class.