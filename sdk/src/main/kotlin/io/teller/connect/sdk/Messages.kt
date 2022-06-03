package io.teller.connect.sdk

import androidx.annotation.Keep
import com.beust.klaxon.TypeAdapter
import com.beust.klaxon.TypeFor
import kotlin.reflect.KClass

@Keep
class MessageTypeAdapter: TypeAdapter<Message> {
    override fun classFor(type: Any): KClass<out Message> = when(type as String?) {
        "ready" -> ReadyMessage::class
        "success" -> SuccessMessage::class
        "failure" -> FailureMessage::class
        "exit" -> ExitMessage::class
        "activityEvent" -> ActivityEventMessage::class
        else -> GenericMessage::class
    }
}

@Keep
data class Data(
    val accessToken: String? = null,
    val user: User? = null,
    val enrollment: Enrollment? = null,
    val payee: Payee? = null,
    val payment: Payment? = null
)

@Keep
data class ActivityEventData(
    val name: String,
    val data: Map<String, Any>
)

@Keep
@TypeFor(field = "event", adapter = MessageTypeAdapter::class)
open class Message(val event: String? = null)

@Keep
class ReadyMessage(): Message()

@Keep
class ExitMessage(): Message()

@Keep
data class SuccessMessage(val data: Data): Message()

@Keep
data class FailureMessage(val type: String, val code: String, val message: String): Message()

@Keep
class ActivityEventMessage(val data: ActivityEventData): Message()

@Keep
data class GenericMessage(val namespace: String?, val data: String?): Message()