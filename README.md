# Dynamic Environment Tool

[![](https://jitpack.io/v/LanarsInc/dynamic-environment-android.svg)](https://jitpack.io/#LanarsInc/dynamic-environment-android)


#### How to
To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```
dependencies {
    implementation 'com.github.LanarsInc:dynamic-environment-android:LATEST'
}
```

**Step 3.** Add OkHttp interceptor

```
  OkHttpClient.Builder()
    .addInterceptor(DynamicEnvironmentInterceptor(context))
    .build()
```

**Step 4.** Call dialog

```
val dialogState = remember { mutableStateOf(false) }

if (dialogState.value) {
    DynamicEnvironmentDialog(
        baseUrl = BuildConfig.AUTH_BASE_URL,
        onDismiss = {
            dialogState.value = false
        }
    )
}
```

