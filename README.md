# BINFO_FCE-14_ShoppingListApp

## Overview
This is a simple Android application for managing a shopping list. The application allows users to view a list of items, add new items, and update existing item details.

## Architecture
The application follows the design principles of the MVMM (Model-View-ModelView) architecture + ROOM database to achieve:

1. **Separation of Concerns**:
   - MVVM architecture separates the concerns of the application into distinct components: Model, View, and ViewModel. This makes the codebase more organized and easier to understand, maintain, and test.
   - ROOM provides a clear separation between database operations (Model) and UI-related logic (ViewModel), further enhancing the modularity of the codebase.

3. **Lifecycle Awareness**:
   - ViewModels are lifecycle-aware components, meaning they are automatically destroyed when their associated View (Activity or Fragment) is destroyed. This prevents memory leaks and ensures that resources are properly managed.
   - LiveData objects exposed by ViewModels automatically update UI components (Views) when the underlying data changes, ensuring that the UI remains up-to-date with the latest data.

4. **UI Responsiveness**:
   - LiveData objects provided by ViewModels allow for efficient data updates in the UI. Only the UI parts affected by data changes are updated, resulting in a more responsive and efficient user experience.

5. **Database Abstraction**:
   - ROOM provides an abstraction layer over SQLite, making it easier to work with databases in Android applications. It simplifies database operations and reduces boilerplate code.

### Roles of the Applications Components:

1. **Model**: `Item.java`
   - Entities: Define the data structure of the application.

2. **ViewModel**: `ItemViewModel.java`
   - Contains the business logic of the application.
   - Interacts with the Model (ROOM database) to retrieve and update data.
   - `ItemViewModelFactory.java` is responsible for creating instances of `ItemViewModel` with custom constructor parameters.
   - Exposes LiveData objects to the View (UI components) for observing data changes ( `ItemAdapter.java`).
   - Handles configuration changes (e.g., screen rotations) without losing data.

3. **View**:
   - Activities and Fragments: Present the user interface to the user and handle user interactions.
      - `MainActivity.java`
      - `SpActivity.java`
      - `AddItemFragment.java`
      - `HomeFrament.java`
   - Layout XML files: Define the layout and appearance of UI components.
      - `activity_main.xml`
      - `ativity_sp.xml`
      - `fragment_add_item.xml`
      - `fragment_edit_item.xml`
      - `fragment_home.xml`
      - `item_layout.xml`
      - `home_menu.xml`
      - `menu_add_item.xml`
      - `menu_edit_item.xml`
   - Drawable, colors, and string resources to complement the layout XML files.

4. **Repository**: `ItemRepository.java`
   - Abstracts the data sources (e.g., local database, network) from the ViewModel, providing a clean API for data access.
   - Coordinates data operations between the ViewModel and the Model (ROOM database).

5. **Database**: `ItemDatabase.java`
   - Holds the application data using ROOM's database implementation.
   - Contains entities and DAOs defined in the Model layer.
   - Handles database operations such as data retrieval, insertion, updating, and deletion. In addition, to custom database operations ((listAllItems, searchItem, etc.).
  
6. **Data Access Objects**: `ItemDao.java`
   Define database operations (e.g., insert, update, delete) and other custom operations (listAllItems, searchItem) for interacting with the database.


### Functionality
_NOTE: Once all information is added or edited, the keyboard must be minimized to access the action button `Done`._

1. **Home Fragment View**:
     - Displays the shopping list using `RecyclerView`, it is scrollable if there are too many items.
     - Displays an `empty shopping bag` if the shopping list is empty.
     - Provides an `Add` Floating button to navigate to the `Add Item View` where users can add new items.
     - Captures click events on the selected and existing item.
        - Click events to navigate to the `Item Detail View` to edit item information.
        - Long-click events to `Delete` the selected item.
     - Provides a menu bar where:
        - The `Search` button to list item(s) based on the item title or description. If there is a result, a new list is displayed. If the input search is cleared, the default list is displayed.

2. **Add Item Fragment View**:
     - Allows users to input item details (title, description, quantity, units, etc.)
     - Provides a `Done` Floating button to save the new item, as long as the Item title is not empty.
     - Provides a menu bar where:
        - The `Home` button can be used to return to the main view.

3. **Item Detail Fragment View**:
     - Displays item details and allows users to edit them.
     - Provides a `Done` Floating button to save changes after editing, as long as the Item title is not empty.
     - Provides a menu bar where:
        - The `Home` button can be used to return to the main view.
        - The `Garbage` button to delete the current item in editing.

### Future improvements

1. Change the app to a List of Shopping lists.
2. Create a Launcher Icon.
