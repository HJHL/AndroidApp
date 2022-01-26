package cs

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * 手机端执行 shell 命令
 *
 * 1. https://github.com/anysou/APP_process
 * 2. https://haruue.moe/blog/2017/08/30/call-privileged-api-with-app-process/
 * */
object CommandService {
    private const val TAG = "CommandService"
    private const val ADDR = "127.0.0.1"
    private const val PORT = 12580

    @JvmStatic
    fun main(args: Array<String>) {
        val server = ServerSocket(PORT)
        Log.d(TAG, "server is running: ${server.inetAddress}:${server.localPort}")

        while (true) {
            val client = server.accept()
            Log.d(TAG, "new client coming: ${client.inetAddress.hostAddress}")

            thread { JobHandler(client) }
        }
    }

    class JobHandler(private val client: Socket) : Runnable {

        companion object {
            private const val DEFAULT_WAIT_TIME = 5000L
        }

        private val reader = Scanner(client.getInputStream())
        private val writer = client.getOutputStream()
        private var running = true

        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            while (running) {
                try {
                    val remoteCommand = reader.nextLine()

                    if (remoteCommand == "QUIT") {
                        shutdown()
                    }
                    val result = executeShellCommand(remoteCommand)
                    write(result)
                } catch (e: Exception) {
                    shutdown()
                } finally {

                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun executeShellCommand(command: String): String {
            try {
                val process = Runtime.getRuntime().exec(command)
                val response = process.inputStream
                process.waitFor(DEFAULT_WAIT_TIME, TimeUnit.MILLISECONDS)
                return readFully(response)
            } catch (e: Exception) {
                Log.w(TAG, e.message, e)
            }
            return ""
        }

        private fun write(message: String) {
            writer.write((message + "\n").toByteArray(Charset.defaultCharset()))
        }

        private fun shutdown() {
            running = false
            client.close()
            Log.d(TAG, "${client.inetAddress.hostAddress} closed the connnection")
        }

        private fun readFully(inputStream: InputStream): String {
            try {
                val baos = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var length = inputStream.read(buffer)
                while (length != -1) {
                    baos.write(buffer, 0, length)
                    length = inputStream.read(buffer)
                }
                return baos.toString(Charset.defaultCharset().name())
            } catch (e: Exception) {
                Log.w(TAG, e.message, e)
            }
            return ""
        }
    }
}