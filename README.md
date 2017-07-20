# asteroids
Kotlin port of ForeignGuyMike's Asteroids project with changes, additional features and improvements.

# Changes, Additional Features, and Improvements

Ported work included followings

* Works with Desktop, iOS, and Android
* Ported completely to Kotlin utilizing several of Kotlin language features
* iOS, and Android build with control (touchpad and etc) suitable for mobile
* Controller support for desktop
* Continued with initial intention of the project by using non-texture for game object, only shape-based via `ShapeRenderer`. Only texture used are bitmap which is dynamically generated in run time from `.ttf` font file via `BitmapFont`.
* Avoid using `java.awt.geom` classes which are `Line2D`, and `Point2D` classes which are not supported on Android by creating a wrapper class with functionality intended to be used in this project; just hold the data.
* Performance improvements
    * Improved draw call over the same type of object but with multiple instances. See [IBatchShapeRenderable](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/interfaces/IBatchShapeRenderable.kt) and [IBatchWraperShapeRenderable](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/interfaces/IBatchWrapperShapeRenderable.kt).
    * Object pools are used to avoid creating new object every time
    * Proper clearing native resource via calling `dispose()` properly
    * [BBInput](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInput.kt), and [BBInputProcessor](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInputProcessor.kt) are iterated from previous projects ([BlockBunny](https://github.com/haxpor/blockbunny), and [OMO](https://github.com/haxpor/omo)) and refactored to represent good enough and working class to handle input all across the game. It supports keyboard, mouse, and controllers


# Gifs in Action

## Gameplay on mobile

Captured on iOS (noted the degraded framerate for gif, it's smooth in real.)
![iOS gameplay gif](https://media.giphy.com/media/54r3o7nmXqhXi/giphy.gif)

## Desktop build with controller support

![controller support](https://media.giphy.com/media/mqouIJtbhh5VS/giphy.gif)

# Notice

* Better utilize more lambda functions of Kotlin when necessary through out the project especially in [Play](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/states/Play.kt)
* [BBInput](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInput.kt) and [BBInputProcessor](https://github.com/haxpor/asteroids/blob/master/core/src/io/wasin/asteroids/handlers/BBInputProcessor.kt) handles all input from keyboard, mouse, and controller (with up to N players, set in source but this game is single player)
* Properly handle to clear native resource via calling `dispose()`
* Some screens check if game is running on Android, or iOS then dynamically add support for mobile interaction

# License
[MIT](https://github.com/haxpor/asteroids/blob/master/LICENSE), Wasin Thonkaew

As ForeignGuyMike didn't mention or include any license to his project but generally publicize through video tutorial, so this project follows along with that intention, thus the decision to be MIT in this case.
