# Internal-Storage-File-Provider (ISFProvider)
This project offers `InternalStorageFileProvider` and client application (IS Browser) to it.
`InternalStorageFileProvider` allows to share securely files inside application private directory (`Context.getDataDir()`) to any other application.
Access to internal files is possible only with Access Token.

# How to use `InternalStorageFileProvider`?
- 1 - Configure `AndroidManifest.xml`
- 2 - Specify Access Token to a directory
- 3 - Run application with `InternalStorageFileProvider`
- 4 - Browse files with client application (IS Browser)

# 1. Configure `AndroidManifest.xml`
Define a provider in your application `AndroidManifest.xml`:
```xml
<application>

    ...

    <provider
        android:name="com.github.ai.fprovider.InternalStorageFileProvider"
        android:authorities="CONTENT_PROVIDER_AUTHORITY"
        android:exported="true" />

    <meta-data
        android:name="com.github.ai.fprovider.provider_authority"
        android:value="CONTENT_PROVIDER_AUTHORITY" />

</application>
```

Define a meta-data with your `InternalStorageTokenManager` authority:
```xml
<application>

    ...

    <provider 
        ...
        />

    <meta-data
        android:name="com.github.ai.fprovider.provider_authority"
        android:value="CONTENT_PROVIDER_AUTHORITY" />

</application>
```

# 2. Specify Access Token to a directory
Get instance of `InternalStorageTokenManager`:
```kotlin
val tokenManger = InternalStorageTokenManager.from(context)
```
Save token for a specified path (path should be relative to a data directory `Context.getDataDir()`):
```kotlin
val TOKEN = "your-secured-token-to-access-the-files"
// Absolute path will be "/data/data/com.your_app_package_name/files/config"
val PATH = "/files/config"
if (tokenManager.getPathByToken(TOKEN) != PATH) {
    tokenManager.addToken(TOKEN, PATH)
}
```
Token requirements:
 - can contain a word characters, a digit characters or minus sign (a-zA-Z0-9_\-)
 - at least 4 characters

Example of valid Token:
```kotlin
UUID.randomUUID().toString()
```

# 3. Run application with `InternalStorageFileProvider`
Install app with configured `InternalStorageFileProvider`.

# 4. Browse files with client application (IS Browser)
4.1 Install "IS Browser" application. It can be installed from [CI page](https://github.com/aivanovski/internal-storage-file-provider/actions)</br>
4.2 Congure "IS Browser" so that it will be able to access to your `InternalStorageFileProvider`. Open settings screen in "IS Browser" and configure listed parameters:
- Root directory path (it is a relative path to a shared directory)
- Access Token
- Content provider authority (your `InternalStorageFileProvider` authority)
