# BINFO_FCE-14_ShoppingListApp

## Overview
This is a simple Android application for managing a shopping list. The application allows users to view a list of items, add new items, and update existing item details.

## Architecture
The application follows the design principles of the MVMM (Model-View-ModelView) architecture + ROOM database to achieve:

1. **Separation of Concerns**:
   - MVVM architecture separates the concerns of the application into distinct components: Model, View, and ViewModel. This makes the codebase more organized and easier to understand, maintain, and test.
   - ROOM provides a clear separation between database operations (Model) and UI-related logic (ViewModel), further enhancing the modularity of the codebase.

2. **Testability**:
   - With MVVM, business logic is encapsulated within the ViewModel, which makes it easier to unit test without relying on Android framework components like Activities or Fragments.
   - ROOM's DAOs can be tested independently of the UI, allowing for comprehensive testing of database operations.

3. **Lifecycle Awareness**:
   - ViewModels are lifecycle-aware components, meaning they are automatically destroyed when their associated View (Activity or Fragment) is destroyed. This prevents memory leaks and ensures that resources are properly managed.
   - LiveData objects exposed by ViewModels automatically update UI components (Views) when the underlying data changes, ensuring that the UI remains up-to-date with the latest data.

4. **UI Responsiveness**:
   - LiveData objects provided by ViewModels allow for efficient data updates in the UI. Only the parts of the UI that are affected by data changes are updated, resulting in a more responsive and efficient user experience.

5. **Database Abstraction**:
   - ROOM provides an abstraction layer over SQLite, making it easier to work with databases in Android applications. It simplifies database operations and reduces boilerplate code, leading to increased developer productivity.

### Roles of the App Components:

1. **Model**:
   - Entities: Define the data structure of the application (e.g., Item entity for the shopping list application).
   - DAOs (Data Access Objects): Define database operations (e.g., insert, update, delete) for interacting with the database.

2. **ViewModel**:
   - Contains the business logic of the application.
   - Interacts with the Model (ROOM database) to retrieve and update data.
   - Exposes LiveData objects to the View (UI components) for observing data changes.
   - Handles configuration changes (e.g., screen rotations) without losing data.

3. **View**:
   - Activities and Fragments: Present the user interface to the user and handle user interactions.
   - Layout XML files: Define the layout and appearance of UI components.

4. **Repository** (Optional, but commonly used):
   - Acts as a single source of truth for data operations.
   - Abstracts the data sources (e.g., local database, network) from the ViewModel, providing a clean API for data access.
   - Coordinates data operations between the ViewModel and the Model (ROOM database).

5. **Database**:
   - Holds the application data using ROOM's database implementation.
   - Contains entities and DAOs defined in the Model layer.
   - Handles database operations such as data retrieval, insertion, updating, and deletion.


### Functionality

1. **Main View**:
  - Displays the shopping list using ListView.
  - Provides an "Add" button to add new items.
2. **Add Item View**:
  - Allows users to input item details (title, description).
  - Provides an "Add" button to save the new item.
3. **Item Detail View**:
  - Displays item details and allows users to edit them.
  - Provides an "Update" button to save changes.
  - Allows users to navigate back to the main view.


