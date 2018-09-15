package com.turastory.shanghycon

import android.util.Log
import com.google.gson.JsonObject
import com.turastory.shanghycon.util.Exec
import com.turastory.shanghycon.util.Util
import org.json.JSONArray
import org.json.JSONObject
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import java.util.concurrent.Future

/**
 * Created by tura on 2018-09-15.
 */
class HyconMiner {
    companion object {

        const val host = "10.10.1.80"
        const val port = 9081
        const val maxNonce = 0xffffffff
    }

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null
    private var scanner: Scanner? = null

    private var future: Future<*>? = null

    fun runServer() {
        stopServer()

        future = Exec.work {
            socket = Socket(host, port).apply {
                printWriter = PrintWriter(this.getOutputStream())
                scanner = Scanner(this.getInputStream())
                this.tcpNoDelay = true
            }

            send(makeSubscribeRequest())
            send(makeAuthorizeRequest())
            le("Miner is running!")
            listen()
        }
    }

    fun stopServer() {
        future?.let {
            if (!it.isCancelled) {
                it.cancel(true)
                le("Stop the miner!")
                future = null
                return
            }
        }
    }

    private fun listen() {
        var miningFuture: Future<*>? = null
        while (true) {
            scanner?.nextLine()?.let {
                val jsonObject = JSONObject(it)

                // 에러 처리
                if (jsonObject.has("error")) {
                    if (!jsonObject.isNull("error")) {
                        le(jsonObject.getJSONObject("error").getString("message"))
                        return
                    }
                }

                // 마이닝
                if (jsonObject.has("method") &&
                    jsonObject.get("method") == "mining.notify") {
                    jsonObject.getJSONArray("params")?.let { array ->
                        if (array.length() < 2)
                            return

                        stopExistingMining(miningFuture)
                        miningFuture = Exec.work {
                            val jobId = array.get(0) as Int
                            le("Working on the new job on Thread ${Thread.currentThread().id}")
                            array[1].let {
                                (1..maxNonce).forEach { nonce ->
                                    val nonceHex = makeNonceHex(nonce.toInt())
                                    send(makeSubmitRequest(jobId, Util.bytesToHex(ByteArray(0)), nonceHex))
                                    if (nonce.toInt() % 50 == 0)
                                        le("submit!! nonce: $nonce")
                                    Thread.sleep(30)
                                }
                            }
                        }

                    } ?: let {
                        le("no param..? what the hell!?")
                    }
                }

                // 보상 수령
                if (jsonObject.has("result")) {
                    if (jsonObject.get("result") is Boolean &&
                        jsonObject.getBoolean("result")) {
                        stopExistingMining(miningFuture)
                        le("Share accepted by the pool!")
                    }
                }
            }

            if (Thread.interrupted())
                break
        }
    }

    private fun stopExistingMining(miningFuture: Future<*>?) {
        var miningFuture1 = miningFuture
        miningFuture1?.let {
            if (!it.isCancelled) {
                it.cancel(true)
                miningFuture1 = null
                return
            }
        }
    }

    private fun makeNonceHex(nonce: Int): String {
        return ByteArray(8).run {
            fill(0.toByte())
            set(4, nonce.toByte())
            set(5, (nonce shr 8).toByte())
            set(6, (nonce shr 16).toByte())
            set(7, (nonce shr 24).toByte())
            Util.bytesToHex(this)
        }
    }

    private fun send(jsonObject: JSONObject) {
        this.printWriter?.let {
            it.print(jsonObject.toString() + "\n")
            it.flush()
        } ?: let {
            le("printWriter is null")
        }
    }

    private fun makeAuthorizeRequest() =
        JSONObject().apply {
            put("method", "mining.authorize")
            put("id", 1)
            put("params", JSONArray().put("test").put("test"))
        }

    private fun makeSubscribeRequest() =
        JSONObject().apply {
            put("method", "mining.subscribe")
            put("id", 1)
            put("params", JSONArray().put("Node.js Stratum"))
        }

    // 사실상 여기서 hashHex는 의미 없음. nonce가 중요함
    private fun makeSubmitRequest(jobId: Int, hashHex: String, nonceHex: String) =
        JSONObject().apply {
            put("method", "mining.submit")
            put("id", "1")
            put("params", JSONObject()
                .put("id", "0")
                .put("job_id", jobId.toString())
                .put("nonce", nonceHex)
                .put("result", hashHex))
        }

    private fun le(s: String) {
        Log.e("HyconMiner", s)
    }
}