# Forge

[![jcenter](https://api.bintray.com/packages/kittinunf/maven/Forge/images/download.svg)](https://bintray.com/kittinunf/maven/Forge/_latestVersion) 
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.30-blue.svg)](http://kotlinlang.org) 
[![Build Status](https://travis-ci.org/kittinunf/Forge.svg?branch=master)](https://travis-ci.org/kittinunf/Forge) 
[![](https://jitpack.io/v/kittinunf/forge.svg)](https://jitpack.io/#kittinunf/forge/) 
[![Codecov](https://codecov.io/github/kittinunf/Forge/coverage.svg?branch=master)](https://codecov.io/gh/kittinunf/Forge)

Forge is a JSON library that helps you map your Kotlin class from a JSON and vice versa in a functional way. Forge is highly inspired by [Aeson](https://hackage.haskell.org/package/aeson), JSON parsing library in Haskell.

## Installation

### Gradle

#### jcenter

```
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.kittinunf.forge:forge:<latest-version>'
}
```

### Usage (tl;dr:)

Given you have JSON as such

``` Json
{
  "id": 1,
  "name": "Clementina DuBuque",
  "age": 46,
  "email": "Rey.Padberg@karina.biz",
  "friends": [
    {
        "id": 11,
        "name": "Ervin Howell",
        "age": 32,
        "email": "Shanna@melissa.tv",
        "friends": []
    }
  ],
  "dogs": [
    {
      "name": "Lucy",
      "breed": "Dachshund",
      "is_male": false
    }
  ]
}
```

You can write your Kotlin class definition as such

``` Kotlin
data class User(val id: Int,
                val name: String,
                val age: Int,
                val email: String?,
                val friends: List<User>,
                val dogs: List<Dog>?)

data class Dog(val name: String, val breed: String, val male: Boolean)

fun userDeserializer(json: JSON) =
    ::User.create.
        map(json at "id").
        apply(json at "name").
        apply(json at "age").
        apply(json maybeAt "email").
        apply(json.list("friends", ::userDeserializer)).  //userDeserializer is a function, use :: as a function reference
        apply(json.maybeList("dogs", dogDeserializer))  //dogDeserializer is a lambda, use it directly

val dogDeserializer = { json: JSON ->
    ::Dog.create.
        map(json at "name").
        apply(json at "breed").
        apply(json at "is_male")
}

```

Viola!, then, you can deserialize your JSON like

``` Kotlin

//jsonContent is when you receive data as a JSON
val result = Forge.modelFromJson(jsonContent, ::userDeserializer)

when (result) {
    DeserializedResult.Success -> {
        val user = result.value
        //success, do something with user
    }

    DeserializedResult.Failure -> {
        val error = result.error
        //failure, do something with error
    }
}

```

You can also serialize you Kotlin class definition as JSON such:

```kotlin
fun userSerializer(user: User) = JSON.Object(mapOf(
    "id" to user.id.toJson(),
    "name" to user.name.toJson(),
    "age" to user.age.toJson(),
    "email" to user.email.maybeJson(),
    "friends" to user.friends.toJson(::userSerializer),
    "dogs" to user.dogs.maybeJson(::dogSerializer)
))

fun dogSerializer(dog: Dog) = JSON.Object(mapOf(
    "name" to dog.name.toJson(),
    "breed" to dog.breed.toJson(),
    "male" to dog.male.toJson()
))

val json = Forge.jsonFromModel(model, ::userSerializer)

json.asString() 
/* ==>
{
  "id": 1,
  "name": "Clementina DuBuque",
  "age": 46,
  "email": "Rey.Padberg@karina.biz",
  "friends": [
    {
        "id": 11,
        "name": "Ervin Howell",
        "age": 32,
        "email": "Shanna@melissa.tv",
        "friends": []
    }
  ],
  "dogs": [
    {
      "name": "Lucy",
      "breed": "Dachshund",
      "is_male": false
    }
  ]
}
*/
``` 

## Credits

Forge is brought to you by [contributors](https://github.com/kittinunf/Fuel/graphs/contributors).

## Licenses

Forge is released under the [MIT](https://opensource.org/licenses/MIT) license.
