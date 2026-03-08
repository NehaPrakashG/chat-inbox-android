# Chat Inbox Android App

A simple **offline-first chat application** built with **Kotlin, Jetpack
Compose, and Room**.

The app loads conversations from a provided JSON file, stores them
locally, and displays a list of conversations and messages. The focus of
this implementation is **clean architecture, reactive data flow, and
predictable state management**.

On first launch, the bundled **`conversations.json`** file is parsed and
used to seed the Room database. After that, the database becomes the
**single source of truth** for the application.

------------------------------------------------------------------------

# Features

### Inbox

-   Displays a list of conversations\
-   Sorted by **most recent activity**
-   Shows conversation name and last updated timestamp

### Conversation

-   Displays messages for a selected conversation
-   Messages sorted **chronologically (oldest first)**
-   Each message shows **text and formatted timestamp**

### Send Message

-   Users can type and send a message
-   Message is saved locally
-   Conversation `lastUpdated` timestamp is updated
-   UI updates automatically through **reactive database flows**

### Offline-First

-   Initial JSON data is seeded into a **Room database**
-   UI observes database changes using **Kotlin Flow**
-   App works fully **without network access**

------------------------------------------------------------------------

# Architecture

The project follows a **layered architecture with clear separation of
concerns**:

    UI (Jetpack Compose)
            ↓
         ViewModel
            ↓
         Repository
            ↓
    Room Database (DAO)

### UI

-   Built with **Jetpack Compose**
-   Observes ViewModel state

### ViewModel

-   Holds UI state
-   Handles user actions
-   Communicates with repository

### Repository

-   Acts as the **data access layer**
-   Handles message creation and updates
-   Executes database operations on an **IO dispatcher**

### Room Database

-   Persists conversations and messages
-   Uses **Flow for reactive updates**
-   Uses **transactions when sending messages**

------------------------------------------------------------------------

# Data Flow

1.  On first launch, the JSON file is parsed and seeded into the Room
    database
2.  Room becomes the **single source of truth**
3.  ViewModels observe database changes using **Kotlin Flow**
4.  UI collects state using `collectAsStateWithLifecycle`
5.  Sending a message writes to the database inside a **transaction**
6.  Database updates automatically propagate to the UI

------------------------------------------------------------------------

# Tech Stack

-   Kotlin
-   Jetpack Compose
-   Room Database
-   Navigation Compose
-   Kotlin Coroutines & Flow
-   JUnit
-   MockK

------------------------------------------------------------------------

# Database Model

### Conversations

| Field | Description |
|------|-------------|
| id | Conversation ID |
| name | Conversation name |
| lastUpdated | Timestamp used for sorting |

### Messages

| Field | Description |
|------|-------------|
| id | Message ID |
| conversationId | Parent conversation |
| text | Message content |
| lastUpdated | Message timestamp |
# Sorting Logic

### Conversations

``` sql
SELECT * FROM conversations
ORDER BY lastUpdated DESC
```

### Messages

``` sql
SELECT * FROM messages
WHERE conversationId = :conversationId
ORDER BY lastUpdated ASC
```

------------------------------------------------------------------------

# Testing

Unit tests were added for ViewModels using:

-   **JUnit**
-   **MockK**
-   **kotlinx-coroutines-test**

Tests verify:

-   ViewModel state updates when repository flows emit new data
-   Blank messages are ignored when sending
-   Repository interactions occur when expected

------------------------------------------------------------------------

# Development Environment

Tested with:

- Android Studio
- Android Emulator: Android 14 (API 34)
- Min SDK: 24
- Target SDK: 34

# Running the App

1.  Clone the repository
2.  Open the project in **Android Studio**
3.  Run the app on an emulator or device

The database will be **seeded from the JSON file on first launch**.

------------------------------------------------------------------------

# Possible Improvements

With more time the following enhancements could be added:

-   Conversation search
-   Sender/receiver message bubbles
-   Pagination for large message lists
-   Network synchronization
-   Compose UI tests
-   Message grouping by date
-   Typing indicators or read receipts
