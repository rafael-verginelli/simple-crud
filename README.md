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
| Module                | Description                                                        |
|-----------------------|--------------------------------------------------------------------|
| **:core_data module** | Allows modules to have access to DTOs.                             |
| **:core_ui module**   | Allows modules to access Compose, ViewModel and Coil dependencies. |

## Feature Modules
### :create module
Creation or Insertion, this module is responsible for getting user input and preparing the produce data to be saved somewhere (cloud, local db, etc).
**Dependencies:**
- :core_data
- :core_ui

### :read module
This module is responsible for fetching the data from some data source and displaying it to the user.
**Dependencies:**
- :core_data
- :core_ui

## ToDo
- [ ] Implement business logic for :create module
- [ ] Implement business logic for :read module
- [ ] Implement Update operation
- [ ] Implement Update delete
- [ ] Add Timber logger
- [ ] Add Coil dependency
- [x] Add Hilt
- [ ] Replace hardcoded strings by strings.xml file
- [ ] Implement Single Activity architecture (Compose Navigation)
- [ ] Create a Test module
- [ ] Add Unit Tests
- [ ] Extract all values from build.gradle files to libs.versions.toml