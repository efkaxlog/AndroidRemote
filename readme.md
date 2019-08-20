#### Remote Control
An Android app that allows the user to control a machine from an Android device.

The server is written in Python and the Android app project design uses the Model-View-Presenter pattern.

**Features**

    - Detection of online servers on local network from Android.
    - Sending key presses to server.
    - Sending mouse clicks to server.
    - Sending commmands to be executed by server e.g. to shutdown machine.
    - Receiving a screenshot of the server machine and displaying it in the app.
    
**Dependencies**

    Server
        - PyAutoGui module for executing mouse/keyboard functions.
        - MSS (Multiple Screen Shots) module for taking screenshots.
        - numpy array library
        
    Android Client App - No external dependencies.
    Minimum SDK version 21. Project uses features from Java 8 so Java 1.8 compiler is required.
    
![App preview image](https://github.com/efkaxlog/AndroidRemote/blob/master/app_preview.jpg)