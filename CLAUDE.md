# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kotlin Multiplatform (KMP) fruit-themed recipes app targeting Android and iOS, using Compose Multiplatform for shared UI. Project name: **FruitsFun**. Package: `org.example.project`.

## Build Commands

```shell
# Set JAVA_HOME if not configured
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

# Android debug build
./gradlew :composeApp:assembleDebug

# Compile only (faster check)
./gradlew :composeApp:compileDebugKotlin

# Run common tests
./gradlew :composeApp:allTests

# iOS: open iosApp/ in Xcode and run from there
```

## Architecture

Single-activity, single-module (`composeApp`) app using Compose Navigation.

**Navigation flow:** Loading Screen (2s) -> Main Screen (bottom tabs) -> Recipe Details

**Source sets:**
- **commonMain** — All shared UI, navigation, data, state, and theme
- **androidMain** — `MainActivity` (portrait-locked) and Android `Platform` actual
- **iosMain** — `MainViewController` hosting Compose UI
- **commonTest** — Shared tests using `kotlin.test`

**Package structure** (`org.example.project`):
- `theme/` — `FruitTheme` with fruit-inspired pastel color palette and custom typography
- `data/` — Data models (`Recipe`, `RecipeCategory`, `Country`, `PlantableFruit`) and `MockData` singleton
- `state/` — `AppState` class holding favorites, planted fruits, and user name as mutable Compose state
- `screens/` — All screen composables: Loading, Main (with bottom nav), Recipes, RecipeDetails, Origins, Garden, Profile

**State sharing:** `AppState` is created in `App.kt` and passed down through composable parameters. Favorites and planted fruits are in-memory (`mutableStateListOf`).

**Bottom navigation tabs:** Recipes (default), Origins, My Garden, Profile

## Key Conventions

- Dependencies managed via Gradle version catalog (`gradle/libs.versions.toml`)
- Navigation: `org.jetbrains.androidx.navigation:navigation-compose`
- Compose resources in `composeApp/src/commonMain/composeResources/`
- Platform-specific code uses Kotlin `expect`/`actual` (see `Platform.kt`)
- ExposedDropdownMenu requires `@OptIn(ExperimentalMaterial3Api::class)`
- JVM target is 11; Gradle configuration cache and build cache are enabled
