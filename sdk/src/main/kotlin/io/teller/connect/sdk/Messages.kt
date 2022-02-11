package io.teller.connect.sdk

import com.beust.klaxon.TypeAdapter
import com.beust.klaxon.TypeFor
import kotlin.reflect.KClass

class MessageTypeAdapter: TypeAdapter<Message> {
    override fun classFor(type: Any): KClass<out Message> = when(type as String?) {
        "ready" -> ReadyMessage::class
        "success" -> SuccessMessage::class
        "failure" -> FailureMessage::class
        "exit" -> ExitMessage::class
        else -> GenericMessage::class
    }
}

data class Data(
    val accessToken: String? = null,
    val user: User? = null,
    val enrollment: Enrollment? = null,
    val payee: Payee? = null,
    val payment: Payment? = null
)

@TypeFor(field = "event", adapter = MessageTypeAdapter::class)
open class Message(val event: String? = null)
class ReadyMessage(): Message()
class ExitMessage(): Message()
data class SuccessMessage(val data: Data): Message()
data class FailureMessage(val type: String, val code: String, val message: String): Message()
data class GenericMessage(val namespace: String?, val data: String?): Message()