## Libraries to use

- Flow for observable data streams
- Coroutines for concurrent operations and asynchronous execution
- Moshi for JSON parsing
- Retrofit for REST client
- OkHttp for networking
- Hilt for DI
- Mockito-kotlin for mocking.
- Glide for image parsing

## Process to follow for setup

If not setup
- AndroidManifest. DONE.
  - Set android:name to application class name.
  - Add internet permission.
- ViewBinding. DONE.
- Hilt. DONE.
  - Retrofit + Moshi + OkHttp modules. DONE.
- Retrofit Api and network models. DONE.

## Process to follow for development

- Start with building the UI, 
  - Main screen
  - RecyclerView ListAdapter
    - Create RecyclerView.ViewHolder subclass
    - Create the DiffUtIl
    - Use the DiffUtil and ViewHolder in the ListAdapter subclass
    - Build and list item
- initialise adapter. DONE
- then connect it to a ViewModel
- Setup a Result class
- Implement the calls to a Repository in the ViewModel using coroutines and store it in a StateFlow.
- Implement the network call in the repository using dispatchers and withContext + try-catch and return it.


## Unit testing process
- Unit test the ViewModel and Repository
- Test the Activity UI in scope of its integration with the ViewModel
