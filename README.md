# Forge

[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.1.4.3-blue.svg)](http://kotlinlang.org) [![Build Status](https://travis-ci.org/kittinunf/Forge.svg?branch=master)](https://travis-ci.org/kittinunf/Forge) [![](https://jitpack.io/v/kittinunf/forge.svg)](https://jitpack.io/#kittinunf/forge/) [![Codecov](https://codecov.io/github/kittinunf/Forge/coverage.svg?branch=master)](https://codecov.io/gh/kittinunf/Forge)

Functional style JSON parsing written in Kotlin

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

```
data class User(val id: Int,
                val name: String,
                val age: Int,
                val email: String?,
                val friends: List<DeserializedResult<User>>,
                val dogs: List<DeserializedResult<Dog>>?)

data class Dog(val name: String, val breed: String, val male: Boolean)

fun userDeserializer(json: JSON) =
    ::User.create.
        map(json at "id").
        apply(json at "name").
        apply(json at "age").
        apply(json maybeAt "email").
        apply(json.list("friends", ::userDeserializer)).
        apply(json.maybeList("dogs", ::dogDeserializer))

val dogDeserializer = { json: JSON ->
    ::Dog.create.
        map(json at "name").
        apply(json at "breed").
        apply(json at "is_male")
}

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


