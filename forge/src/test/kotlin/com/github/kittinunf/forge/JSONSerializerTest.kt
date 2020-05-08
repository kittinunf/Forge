package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.Serializable
import com.github.kittinunf.forge.serializer.maybeJson
import com.github.kittinunf.forge.serializer.toJson
import com.github.kittinunf.forge.stringifier.asString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class JSONSerializerTest {

    @Test
    fun testJsonSerializerFun() {

        fun leafNodeSerializer(leafNode: LeafNode): JSON = JSON.String(leafNode.javaClass.simpleName)

        fun serializer(model: Model): JSON =
                JSON.Object(mapOf(
                        "string" to model.string.toJson(),
                        "int" to model.int.toJson(),
                        "boolean" to model.boolean.toJson(),
                        "optionalField" to model.optionalField.maybeJson(::serializer),
                        "mandatoryField" to model.mandatoryField.toJson(::leafNodeSerializer),
                        "collection" to model.collection.toJson(::serializer)
                ))

        val serializedJson: JSON = TestData.model.toJson(::serializer)

        assertThat(serializedJson.asString(), equalTo(TestData.modelJsonRepresentation.asString()))
    }

    @Test
    fun testJsonSerializerComp() {

        val serializedJson: JSON = TestData.model.toJson(Model.serializer)

        assertThat(serializedJson.asString(), equalTo(TestData.modelJsonRepresentation.asString()))
    }

    @Test
    fun testJsonSerializerForge() {

        val serializedJson: JSON = Forge.jsonFromModel(TestData.model, Model.serializer)

        assertThat(serializedJson.asString(), equalTo(TestData.modelJsonRepresentation.asString()))
    }

    @Test
    fun testJsonSerializerSerializable() {

        class ModelSerializable: Serializable<Model> {
            override fun serialize(model: Model): JSON = Model.serializer(model)
        }

        val serializedJson: JSON = Forge.jsonFromModel(TestData.model, ModelSerializable())

        assertThat(serializedJson.asString(), equalTo(TestData.modelJsonRepresentation.asString()))
    }

    companion object {

        private object LeafNode {

            val serializer: (LeafNode) -> JSON = { leafNode ->
                JSON.String(leafNode.javaClass.simpleName)
            }
        }

        private data class Model(val string: String,
                                 val int: Int,
                                 val boolean: Boolean,
                                 val optionalField: Model?,
                                 val mandatoryField: LeafNode,
                                 val collection: List<Model>) {

            companion object {

                val serializer: (Model) -> JSON = { model ->
                    JSON.Object(mapOf(
                            "string" to model.string.toJson(),
                            "int" to model.int.toJson(),
                            "boolean" to model.boolean.toJson(),
                            "optionalField" to model.optionalField.maybeJson(Model.serializer),
                            "mandatoryField" to model.mandatoryField.toJson(LeafNode.serializer),
                            "collection" to model.collection.toJson(Model.serializer)
                    ))
                }
            }
        }

        private object TestData {

            val model = Model(
                    string = "string",
                    int = 1,
                    boolean = false,
                    optionalField = null,
                    mandatoryField = LeafNode,
                    collection = listOf(
                            Model(
                                    string = "string2",
                                    int = 2,
                                    boolean = true,
                                    optionalField = null,
                                    mandatoryField = LeafNode,
                                    collection = listOf()
                            )
                    )
            )

            val modelJsonRepresentation: JSON = JSON.Object(mapOf(
                    "string" to JSON.String(model.string),
                    "int" to JSON.Number(model.int),
                    "boolean" to JSON.Boolean(model.boolean),
                    "optionalField" to JSON.Null(),
                    "mandatoryField" to JSON.String("LeafNode"),
                    "collection" to JSON.Array(listOf(
                            JSON.Object(mapOf(
                                    "string" to JSON.String(model.collection.first().string),
                                    "int" to JSON.Number(model.collection.first().int),
                                    "boolean" to JSON.Boolean(model.collection.first().boolean),
                                    "optionalField" to JSON.Null(),
                                    "mandatoryField" to JSON.String("LeafNode"),
                                    "collection" to JSON.Array()
                            ))
                    ))
            ))
        }
    }
}
