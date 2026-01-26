# App Architecture

This document describes the overall architecture of the app, focusing on
Jetpack Compose, feature-first UI organization, and clear separation of concerns.

The architecture is designed to be:
- Easy to navigate
- Easy to extend
- Hard to accidentally break

---

## 🧱 Architectural Principles

The app follows these core principles:

- UI-driven architecture (Compose-first)
- Feature-first package organization
- Stateless composables by default
- Explicit state management
- One-directional data flow
- Minimal coupling between layers

If something feels magical, it probably does not belong here.

---

## 📁 Package Structure

High-level structure:

- ui
  - navigation
  - screens
    - homescreen
    - downloads
    - mainscreen
  - theme
- util

Each layer has a single, well-defined responsibility.

---

## 🧭 Navigation Layer

Location:
- ui/navigation

Responsibilities:
- Central navigation graph
- Route definitions
- Screen-to-screen transitions

Rules:
- Navigation logic stays centralized
- Screens do not navigate directly to each other
- Routes are explicit and predictable

Navigation exists to move between screens, not to surprise developers.

---

## 🖥️ Screens Layer

Location:
- ui/screens

Each screen:
- Represents a full UI surface
- Owns its screen-level state
- Coordinates UI components

Examples:
- HomeScreen
- DownloadsScreen
- MainScreen

Screens are allowed to be complex.
Components are not.

---

## 🧩 Components Layer

Location:
- ui/screens/**/components

Components are:
- Stateless
- Reusable
- UI-only

Components must not:
- Perform navigation
- Hold business logic
- Access repositories
- Know where their data comes from

If a component needs a ViewModel, it is no longer a component.

---

## 🎨 Theme Layer

Location:
- ui/theme

Responsible for:
- Colors
- Typography
- Shapes
- Material theme setup

Theme changes should propagate automatically across the app.

---

## 🧰 Utility Layer

Location:
- util

Contains:
- Permission helpers
- Platform-specific utilities
- Cross-cutting helpers

Rules:
- No UI code
- No navigation
- No stateful business logic

Utilities help everything, but own nothing.

---

## 🚀 Entry Points

Application:
- App.kt
- Initializes Hilt
- Sets up app-wide configuration

Activity:
- MainActivity.kt
- Hosts Compose content
- Attaches the navigation root

The activity is a container, not a logic hub.

---

## 🔄 Data & State Flow

The app follows one-directional data flow:

User action
→ Composable
→ ViewModel
→ Repository or data source
→ State update
→ UI recomposition

Rules:
- State drives UI
- UI does not mutate state directly
- No hidden side effects

If the UI changes, there should be a visible reason.

---

## 🧭 Architecture Philosophy

This architecture prioritizes:
- Readability over clever tricks
- Explicit structure over smart shortcuts
- Scalability over speed hacks
- Compose-first patterns

If adding a feature feels painful, the architecture needs improvement,
not more comments.

---

## 🚧 Notes

- New screens must follow existing patterns (creativity is welcome elsewhere)
- Components should remain stateless (state leaks are not clever)
- Navigation logic must stay centralized (no surprise teleports)
- Architecture documentation evolves with the codebase

The goal is simple:
Future you should read this without opening Git blame in anger.
