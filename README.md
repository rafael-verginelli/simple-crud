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

### :features:read module
This module is responsible for fetching the data from some data source and displaying it to the user.
**Dependencies:**
- :core_ui
- :core_domain
- :core_data
- :core_testing

### :features:details module
This module is responsible for displaying the data of a single item to the user, as well as triggering edition and deletion of that item.
**Dependencies:**
- :core_ui
- :core_domain
- :core_data
- :core_testing

## BaseViewModel
[BaseViewModel] is a class that should be implemented by all ViewModels with the objective of keeping the behaviours uniform. That way it becomes easier to test functionalities. It consists in an abstract class that requires the implementation of 3 Generic types: the [UiState], the [ViewEvent] and the [ViewModelEffect].
### [UiState]
As the name describes, it is the current state of the Ui. Should be observed by the Views so they can update their state accordingly.
### [ViewEvent]
Those are events triggered by a view. Could be a button click, a scroll up or down, etc. It is how the View communicate to the ViewModel that the user interacted with that View and something should be triggered.
### [ViewModelEffect]
Those are side-effects, no necessarily triggered by a view, but a consequence of some other operation triggered by the ViewModel. For example, a snackbar showing up after some async operation happened.

When unit testing, we can observe the behaviours of [UiState] and [ViewModelEffect], verify if they happen and in which order they happen, and we can use [ViewEvent] to setup the ViewModel states as we need.
Note: currently I'm not able to test if certain [ViewEvent] happened, this is a To-Do!

## ToDo
- [x] Add Timber logger
- [ ] Add Coil dependency
- [ ] Add a Linter
- [ ] Unit test Compose Navigation
- [ ] Implement proper Error Handling for ViewModels
- [ ] Find a solution for feature dependencies on features
- [ ] Review unit test function names
- [ ] Verify why SingletonComponent (UserFakeDataSourceImpl) is being instantiated multiple times
- [ ] Resolve ToDos
- [x] Fix issue where keyboard instances stack when focusing on the different Text Inputs on :create module (couldn't reproduce) 
- [x] Change ViewModel in Composable screens to HiltViewModel
- [x] Fix Navigation routes (wrongly stacking, missing popUpTo() calls)
- [x] Edit Operation, when finished successfully, should NavigateUp to User Details
- [x] Fix Edit Screen title
- [ ] Use Flow in Repos to instantly reflect changes to all observers
- [ ] After editing, going back to Details doesn't show updated registry (flow in repo task should solve it)
- [ ] After editing, going back to Details then to List doesn't show updated registry (flow in repo task should solve it)
- [ ] After deleting, List doesn't show changes (flow in repo task should solve it)
- [ ] In insertion, label "name" has ":" in front but age and email don't
- [ ] When editing a registry, title of the page is still "create" instead of "edit"
- [ ] When transitioning from list of users to edit user, "User not found" is displayed, it would be better to leave nothing until loading comes up
- [ ] After editing, going back to Details has no arrow, must rely on native back arrow
- [ ] Back arrow in Details doesn't work and is a bit too small
- [ ] Nice to have: Pull to refresh
- [ ] Add timber to BaseViewModel
- [ ] Allow Unit Tests to check whether a [ViewEvent] happened or not
- [ ] Navigating back with Native Back Arrow from Insertion to Home (Read) doesn't change selected tab in Bottom Navigation Bar
- [ ] Prevent current open tab to open if already opened in Bottom Nav Bar
- [ ] Delete and Update snackbars are not displayed because VM is killed when navigating up