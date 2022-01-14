# Forge

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.0-blue.svg)](http://kotlinlang.org)
[![jcenter](https://api.bintray.com/packages/kittinunf/maven/Forge/images/download.svg)](https://bintray.com/kittinunf/maven/Forge/_latestVersion)
[![MavenCentral](https://maven-badges.herokuapp.com/maven-central/com.github.kittinunf.forge/forge/badge.svg)](https://search.maven.org/search?q=g:com.github.kittinunf.forge)
[![Build Status](https://travis-ci.org/kittinunf/Forge.svg?branch=master)](https://travis-ci.org/kittinunf/Forge)
[![](https://jitpack.io/v/kittinunf/forge.svg)](https://jitpack.io/#kittinunf/forge/) 
[![Codecov](https://codecov.io/github/kittinunf/Forge/coverage.svg?branch=master)](https://codecov.io/gh/kittinunf/Forge)

Forge is a JSON library that helps you map your Kotlin class from a JSON and vice versa in a functional way. Forge is highly inspired by [Aeson](https://hackage.haskell.org/package/aeson), JSON parsing library in Haskell.

## Ideology

Have you ever wonder that how other JSON libraries out there work? Magic under the hood? or a complex annnotation processing? If that is something that you don't want, with Forge, we don't do any of those. 

Forge aims to provide a full control over how to parse JSON into a Kotlin class, no more magic, no more annotation.

## Installation

### Gradle

```
repositories {
    jcenter() //or mavenCentral()
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
  "phone": {
    "name": "My Phone",
    "model": "Pixel 3XL"
  },
  "friends": [
    {
        "id": 11,
        "name": "Ervin Howell",
        "age": 32,
        "email": "Shanna@melissa.tv",
        "phone": {
            "name": "My iPhone",
            "model": "iPhone X"
        },
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
                val phone: Phone,
                val friends: List<User>,
                val dogs: List<Dog>?)

data class Phone(val name: String, val model: String)
data class Dog(val name: String, val breed: String, val male: Boolean)

fun userDeserializer(json: JSON) =
    ::User.create.
        map(json at "id").
        apply(json at "name").
        apply(json at "age").
        apply(json maybeAt "email").
        apply(json.at("phone", phoneDeserializer)),  // phoneDeserializer is a lambda, use it directly
        apply(json.list("friends", ::userDeserializer)).  //userDeserializer is a function, use :: as a function reference
        apply(json.maybeList("dogs", dogDeserializer))

val phoneDeserializer = { json: JSON ->
    ::Phone.create.
        map(json at "name").
        apply(json at "model")
}

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
