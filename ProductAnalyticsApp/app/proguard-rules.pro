# =========================
# ✅ Glide
# =========================
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

# =========================
# ✅ Retrofit + Gson
# =========================
-keep class com.google.gson.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# =========================
# ✅ MPAndroidChart
# =========================
-keep class com.github.mikephil.charting.** { *; }

# =========================
# ✅ ViewModel & Lifecycle
# =========================
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# =========================
# ✅ Coroutines (aman default, tapi untuk safety)
# =========================
-dontwarn kotlinx.coroutines.**

# =========================
# ✅ Navigation (optional)
# =========================
-keep class androidx.navigation.** { *; }

# =========================
# ✅ Optional Debug Info (jika ingin stacktrace tetap jelas)
# =========================
-keepattributes SourceFile,LineNumberTable

# =========================
# ❌ Jangan rename resource class (R)
# =========================
-keep class **.R$* {
    *;
}
