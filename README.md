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
| Module                   | Description                                                        |
|--------------------------|--------------------------------------------------------------------|
| **:core_data module**    | Allows modules to have access to DTOs.                             |
| **:core_ui module**      | Allows modules to access Compose, ViewModel and Coil dependencies. |
| **:core_domain module**  | Allows modules to access shared business logic.                    |
| **:core_testing module** | Allows modules to access [TestCoroutineRule].                      |

## Feature Modules
### :create module
Creation or Insertion, this module is responsible for getting user input and preparing the produce data to be saved somewhere (cloud, local db, etc).
**Dependencies:**
- :core_data
- :core_ui
- :core_testing

### :read module
This module is responsible for fetching the data from some data source and displaying it to the user.
**Dependencies:**
- :core_data
- :core_ui
- :core_testing

## ToDo
- [ ] Implement loading for :create and :read modules Screen
- [ ] Implement Update operation in :details module
- [ ] Implement Update delete in :details module
- [ ] Implement Single Activity architecture (Compose Navigation)
- [ ] Add Timber logger
- [ ] Add Coil dependency
- [ ] Add a Linter
- [ ] Add Unit Tests (in progress)
- [ ] Fix issue where keyboard instances stack when focusing on the different Text Inputs on :create module
- [x] Implement business logic for :create module
- [x] Implement business logic for :read module
- [x] Create :core_domain module for sharing business logic
- [x] Create testing module for sharing coroutine rules file
- [x] Add Hilt
- [x] Replace hardcoded strings by strings.xml file
- [x] Extract all values from build.gradle files to libs.versions.toml
- [x] Add GitHub Actions for running unit tests when PRs are created
- [x] Cleanup pro guard files