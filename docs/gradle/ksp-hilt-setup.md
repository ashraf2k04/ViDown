# Hilt + KSP Setup

This document explains how **Hilt** is configured using **KSP (Kotlin Symbol Processing)**
in this project, including *what is used*, *why it is used*, and *how everything is wired together*.

The setup intentionally avoids **KAPT** in favor of **KSP** for better build performance
and modern Android compatibility.

---

## 🧠 Overview

Hilt is used for **dependency injection**, and KSP is used for **annotation processing**.

This combination provides:
- Faster builds
- Better incremental compilation
- Lower memory usage
- First-class support for Jetpack Compose and Room

---

## 🔌 Plugins Configuration

### Top-level Gradle

Hilt and KSP plugins are declared at the root level but **not applied** immediately.

```kotlin
id("com.google.dagger.hilt.android") version "2.57.2" apply false
id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
```

## ❓ Why This Approach


- Centralizes plugin versions
- Avoids duplication across modules
- Allows selective plugin usage
- Makes multi-module expansion easier


---


## 📱 App Module Setup


The `app` module applies both **Hilt** and **KSP** explicitly.


### Applied Plugins

```kotlin
plugins {
id("com.google.dagger.hilt.android")
id("com.google.devtools.ksp")
}
```

## 📦 Dependencies

### Hilt Runtime
```kotlin
implementation(libs.bundles.hilt)
```
### Hilt Compiler (KSP)
```kotlin
ksp(libs.hilt.compiler)
```
---

## ❓ Why KSP instead of KAPT

| Aspect              | KSP                   | KAPT              |
|---------------------|-----------------------|-------------------|
| Build speed         | Faster                | Slower            |
| Incremental builds  | Better                | Limited           |
| Memory usage        | Lower                 | Higher            |
| Compose support     | Recommended            | Legacy            |
| Future support      | Actively developed    | Maintenance mode  |

* KAPT is intentionally not used in this project.

---

## 🚀 Application Class Setup

Hilt requires an```kotlin  Application ``` class annotated with ```kotlin @HiltAndroidAp` ```.

```kotlin
@HiltAndroidApp
class App : Application()
```
### Purpose
- Triggers Hilt code generation
- Acts as the root of the dependency graph
- Required for all Hilt-enabled apps

---

## 🏗️ Android Components Injection

### Activities
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```
### ViewModels
```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
```
### Other Android Components
- Services
- BroadcastReceivers
- Workers

* All can be injected using `@AndroidEntryPoint`.

---

## 🧩 Modules & Bindings

Dependencies are provided using Hilt modules.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRepository(): Repository = RepositoryImpl()
}
```
### Installation Scopes
- SingletonComponent
- ActivityComponent
- ViewModelComponent
- ServiceComponent

* Choose the scope based on lifecycle requirements.

---

## 🧪 Testing Considerations

- Hilt supports test-specific modules
- KSP-generated code works seamlessly in tests
- No special configuration required beyond standard Hilt testing rules

---

## ⚠️ Common Pitfalls

- Forgetting to apply the KSP plugin
- Using kapt() instead of ksp()
- Missing @HiltAndroidApp on Application
- Mixing KAPT and KSP in the same module

---

## 🧭 Design Philosophy

This Hilt + KSP setup prioritizes:
- Modern Android tooling
- Faster feedback during development
- Explicit configuration
- Long-term maintainability

* Dependency injection should be invisible, predictable, and safe.

---

## 🚧 Notes

- Do not introduce KAPT unless absolutely required
- All new modules should use KSP-based processors
- Keep DI configuration minimal and explicit
- Documentation evolves alongside implementation
