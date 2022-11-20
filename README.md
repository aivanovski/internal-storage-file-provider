# Internal-Storage-File-Provider (ISFProvider)
[![](https://jitpack.io/v/aivanovski/internal-storage-file-provider.svg)](https://jitpack.io/#aivanovski/internal-storage-file-provider) ![Coverage](.github/badges/jacoco.svg)</br>

This project offers `InternalStorageFileProvider` and client application (IS Browser) to it.
`InternalStorageFileProvider` allows to share securely files inside application private directory (`Context.getDataDir()`) to any other application.
Access to internal files is possible only with Access Token.

## Usage
- 1 - Specify dependency in `build.gradle`
- 2 - Configure `AndroidManifest.xml`
- 3 - Specify Access Token to a directory
- 4 - Install application with `InternalStorageFileProvider`
- 5 - Browse files with client application (IS Browser)

## Demo
Sub-project `demo/` demonstrates the usage of `InternalStorageFileProvider`.

## 1. Specify dependency in `build.gradle`
This Android library project is available as AAR in [Jitpack](https://jitpack.io/#aivanovski/internal-storage-file-provider) repository
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.aivanovski:internal-storage-file-provider:X.X.X"
}
```

## 2. Configure `AndroidManifest.xml`
Define a provider in your application `AndroidManifest.xml`:
```xml
<application>

    ...

    <provider
        android:name="com.github.ai.isfprovider.InternalStorageFileProvider"
        android:authorities="CONTENT_PROVIDER_AUTHORITY"
        android:exported="true" />

    <meta-data
        android:name="com.github.ai.isfprovider.provider_authority"
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
        android:name="com.github.ai.isfprovider.provider_authority"
        android:value="CONTENT_PROVIDER_AUTHORITY" />

</application>
```

## 3. Specify Access Token to a directory
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

## 4. Install application with `InternalStorageFileProvider`
Install and run app with configured `InternalStorageFileProvider`.

## 5. Browse files with client application (IS Browser)
#### 5.1 Install "IS Browser" application
Build can be downloaded from [CI page](https://github.com/aivanovski/internal-storage-file-provider/actions)</br> or build it from sources `is-browser/`
#### 5.2 Configure "IS Browser" to access to your `InternalStorageFileProvider`
Open settings screen in "IS Browser" and configure listed parameters:
- Root directory path (it is a relative path to a shared directory)
- Access Token
- Content provider authority (your `InternalStorageFileProvider` authority)
