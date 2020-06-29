<a href="https://github.com/haxpor/donate"><img src="https://img.shields.io/badge/$-donate-ff69b4.svg?maxAge=2592000&amp;style=flat" alt="donate"></a>

# asteroids
Kotlin port of ForeignGuyMike's Asteroids project with changes, additional features and improvements.

# Gifs in Action

## Gameplay on mobile

Captured on iOS (noted the degraded framerate for gif, it's smooth in real.)
![iOS gameplay gif](https://media.giphy.com/media/54r3o7nmXqhXi/giphy.gif)

## Controller support on Desktop

![controller support](http://i.imgur.com/O2sSXq1.gif)
![high score controller interaction](http://i.imgur.com/of6n3Cx.gif)

# Video in Action

Click on image to watch video.

[![desktop gameplay video](http://i.imgur.com/uLRDqkM.png)](https://www.youtube.com/watch?v=25N9RvHNJbQ)
[![mobile gameplay video](http://i.imgur.com/XZ3b3wQ.png)](https://www.youtube.com/watch?v=nHy8_HYIExI)

# Changes, Additional Features, and Improvements

Ported work included followings

* Works with Desktop, ~~iOS (via MOE)~~ (as MOE support was removed in libgdx), and Android
* Ported completely to Kotlin utilizing several of Kotlin language features
* Mobile build with control (touchpad and etc) suitable for mobile
* Controller support for desktop; but fixed to use Xbox360 layout. If you use another brand, you have to edit code for button mapping.
* Continued with initial intention of the project by using non-texture for game object, only shape-based via `ShapeRenderer`. Only texture used are bitmap which is dynamically generated in run time from `.ttf` font file via `BitmapFont`.
* Avoid using `java.awt.geom` classes which are `Line2D`, and `Point2D` classes which are not supported on Android by creating a wrapper class with functionality intended to be used in this project; just hold the data.
* [BBInput](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInput.kt), and [BBInputProcessor](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInputProcessor.kt) are iterated from previous projects ([BlockBunny](https://github.com/haxpor/blockbunny), and [OMO](https://github.com/haxpor/omo)) and refactored to represent much better class to handle input all across the game. It supports keyboard, mouse, and controllers
* Performance improvements
    * Improved draw call over the same type of object but with multiple instances. See [IBatchShapeRenderable](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/interfaces/IBatchShapeRenderable.kt) and [IBatchWraperShapeRenderable](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/interfaces/IBatchWrapperShapeRenderable.kt).
    * Object pools are used to avoid creating new object every time
    * Proper clearing native resource via calling `dispose()` properly

# Notice

* Better utilize more lambda functions of Kotlin when necessary through out the project especially in [Play](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/states/Play.kt)
* [BBInput](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInput.kt) and [BBInputProcessor](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInputProcessor.kt) handles all input from keyboard, mouse, and controller (with up to N players, set in source but this game is single player)
* Properly handle to clear native resource via calling `dispose()`
* Some screens check if game is running on mobile platform then dynamically add support for mobile interaction

# License
[MIT](https://github.com/haxpor/asteroids/blob/master/LICENSE), Wasin Thonkaew

As ForeignGuyMike didn't mention or include any license to his project but generally publicize through video tutorial, so this project follows along with that intention, thus the decision to be MIT in this case.
