package some.external.dependency

import android.os.Handler
import android.os.Looper
import android.os.Message

class LooperDependent {
    private val HANDLER: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
        }
    }
}