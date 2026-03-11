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

```
ui
 ├ navigation
 ├ screens
 │   ├ homescreen
 │   │   ├ components
 │   │   ├ model
 │   │   ├ utils
 │   │   ├ HomeScreen.kt
 │   │   └ HomeViewModel.kt
 │   │
 │   ├ downloads
 │   │   ├ components
 │   │   ├ model
 │   │   ├ utils
 │   │   ├ DownloadsScreen.kt
 │   │   └ DownloadsViewModel.kt
 │   │
 │   ├ playlist
 │   │   ├ state
 │   │   │   ├ content
 │   │   │   ├ loading
 │   │   │   └ error
 │   │   ├ model
 │   │   ├ mapper
 │   │   ├ PlaylistScreen.kt
 │   │   └ PlaylistViewModel.kt
 │   │
 │   └ mainscreen
 │
 └ theme
util
```

Each screen is treated as a self-contained feature module inside the UI layer.

---

## 🧭 Navigation Layer

Location - ` ui/navigation `

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

Location - ` ui/screens `

Each screen:
- Represents a full UI surface
- Owns its screen-level state
- Coordinates UI components

A screen typically contains:
- Screen composable
- ViewModel
- UI components
- UI state models
- Screen-specific utilities

Examples:
```
screens
 └ homescreen
      components/
      model/
      utils/
      HomeScreen.kt
      HomeViewModel.kt
```

Responsibilities:
- Connect ViewModel state to UI
- Coordinate components
- Handle user interactions

Screens are allowed to be complex.
Components are not.

---

## 🧩 Components

Location - ` ui/screens/**/components `

Components are small UI building blocks.

### Characteristics

- Stateless  
- Reusable within the screen  
- Focused only on UI rendering  

### Components must not

- Perform navigation  
- Hold business logic  
- Access repositories  
- Know where their data comes from  

If a component needs a ViewModel, it is no longer a component.

---

## 🧠 Models

Locatio - ` ui/screens/**/model `

Contains UI-specific data models, such as:

- UI state classes  
- UI item models  
- Enums used by the screen  

### Examples

```
- HomeUiState 
- DownloadUiItem  
- PlaylistItemUi
```

These models exist purely to describe UI state, not domain logic.

---

## 🧰 Screen Utilities

Location - ` ui/screens/**/utils `

Contains small helpers used only by that screen.

### Examples

```
- formatBytes helpers  
- selector builders  
- UI formatting utilities  
```
### Rules

- No navigation logic  
- No repositories  
- No global state  

If a utility becomes widely used, it can move to the global ?util? package.

---

## 🎨 Theme Layer

Location - `ui/theme`

Responsible for:

- Colors  
- Typography  
- Shapes  
- Material theme setup  

Theme changes should propagate automatically across the app.

---

## 🧰 Global Utilities

Location: `util`

Contains cross-cutting helpers used across the application.

### Examples
```
- Permission helpers  
- Platform utilities  
- Shared formatting helpers  
```

### Rules

- No UI code  
- No navigation  
- No stateful business logic  

Utilities help everything, but own nothing.

---

## 🚀 Entry Points

### Application  

` App.kt `

- Initializes Hilt  
- Sets up app-wide configuration  

### Activity

` MainActivity.kt `

- Hosts Compose content  
- Attaches the navigation root  

The activity is a container, not a logic hub.

---

## 🔄 Data & State Flow

The app follows one-directional data flow.

Flow:

User action  
→ Composable  
→ ViewModel  
→ Repository or data source  
→ State update  
→ UI recomposition  

### Rules

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

- New screens must follow the existing screen structure  
- Components should remain stateless  
- Navigation logic must stay centralized  
- Architecture documentation evolves alongside the codebase  

The goal is simple:

Future you should read this without opening Git blame in anger.

The goal is simple:
Future you should read this without opening Git blame in anger.
