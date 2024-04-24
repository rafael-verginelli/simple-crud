# Simple CRUD Project
This project has as objective implement a very simple CRUD to serve as base to other projects.

## Package Structure
Each module is separated in 3 packages: **Data**, **Domain** and **UI**.

| Package    | Description                                                                                           |
|------------|-------------------------------------------------------------------------------------------------------|
| **Data**   | Handles DTOs and other models, basically all the information provided by the models are handled here. |
| **Domain** | Contains the main business logic of the application. Versatile and testable.                          |
| **UI**     | Handles the visual part, what will be displayed to the user.                                          |

## Core Modules
| Module                        | Description                                                        |
|-------------------------------|--------------------------------------------------------------------|
| **:core:core_data module**    | Allows modules to have access to DTOs.                             |
| **:core:core_ui module**      | Allows modules to access Compose, ViewModel and Coil dependencies. |
| **:core:core_domain module**  | Allows modules to access shared business logic.                    |
| **:core:core_testing module** | Allows modules to access [TestCoroutineRule].                      |

## Feature Modules
### :features:create module
Creation or Insertion, this module is responsible for getting user input and preparing the produce data to be saved somewhere (cloud, local db, etc).
**Dependencies:**
- :core_ui
- :core_domain
- :core_data
- :core_testing
- 
### :features:read module
This module is responsible for fetching the data from some data source and displaying it to the user.
**Dependencies:**
- :core_ui
- :core_domain
- :core_data
- :core_testing
- 
### :features:details module
This module is responsible for displaying the data of a single item to the user, as well as triggering edition and deletion of that item.
**Dependencies:**
- :core_ui
- :core_domain
- :core_data
- :core_testing
- 
## ToDo
- [ ] Implement loading for :create and :read modules Screen
- [ ] Implement Update operation in :details module
- [ ] Implement Update delete in :details module
- [ ] Add Timber logger
- [ ] Add Coil dependency
- [ ] Add a Linter
- [ ] Add Unit Tests (in progress)
- [ ] Unit test Compose Navigation
- [ ] Fix issue where keyboard instances stack when focusing on the different Text Inputs on :create module
- [ ] Change ViewModel in Composable screens to HiltViewModel
- [ ] Fix Navigation routes
- [ ] Toggle Bottom Navigation Bar according to the screen
- [ ] Implement proper Error Handling for ViewModels
- [ ] Implement fake data source
- [ ] Find a solution for feature dependencies on features
- [ ] Review unit test function names
- [x] Implement Single Activity architecture (Compose Navigation)
- [x] Implement business logic for :create module
- [x] Implement business logic for :read module
- [x] Create :core_domain module for sharing business logic
- [x] Create testing module for sharing coroutine rules file
- [x] Add Hilt
- [x] Replace hardcoded strings by strings.xml file
- [x] Extract all values from build.gradle files to libs.versions.toml
- [x] Add GitHub Actions for running unit tests when PRs are created
- [x] Cleanup pro guard files