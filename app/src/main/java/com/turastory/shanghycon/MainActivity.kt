package com.turastory.shanghycon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.turastory.shanghycon.Util.bytesToHex
import com.turastory.shanghycon.Util.hexStringToByteArray
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import java.util.concurrent.Future


class MainActivity : AppCompatActivity() {

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("hasher")
        }

        const val host = "10.10.1.80"
        const val port = 9081
        const val maxNonce = 0xffffffff
    }

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null
    private var scanner: Scanner? = null

    @Volatile
    private var output = ByteArray(32)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        connectButton.setOnClickListener {
            Thread {
                socket = Socket(host, port).apply {
                    printWriter = PrintWriter(this.getOutputStream())
                    scanner = Scanner(this.getInputStream())
                    this.tcpNoDelay = true
                }

                send(makeSubscribeRequest())
                send(makeAuthorizeRequest())
                listen()
            }.start()
        }

        disconnectButton.setOnClickListener {
            val output = runHash("baf726c86fa097f9d41a4d9d0e316da931902b72bcbf10e079aa24f387b04036ccd90c01d31234c2db4c52b19af0dfa3f205d9896d044ae99d9db90991fe1261000000005c040000")
            le("result: ${bytesToHex(output)}")
        }

        calculateButton.setOnClickListener {
            val output = runHash("e001088654afc43f22ae8200389e5beea354a5a7f7c471d29cefe8479332a1ad437b591a45d993ac595d837f599041748aaf0ffca3d36569bd14fa54d5a2dc44000000000c000000")
            le("result: ${bytesToHex(output)}")
        }
    }

    private fun listen() {
        var future: Future<*>? = null

        while (true) {
            scanner?.nextLine()?.let {
                le(it)

                val jsonObject = JSONObject(it)

                if (jsonObject.has("error")) {
                    if (!jsonObject.isNull("error")) {
                        le(jsonObject.getJSONObject("error").getString("message"))
                        return
                    }
                }

                if (jsonObject.has("method") &&
                    jsonObject.get("method") == "mining.notify") {
                    jsonObject.getJSONArray("params")?.let { array ->
                        if (array.length() < 3)
                            return

                        // Use another thread to solve the hash problem.
                        // -> we get instance of future here, so we can stop the task later.
                        val target = array[2] as String

                        array[1].let { prevHash ->
                            val prevHex = (prevHash as String)
                            (1..5).forEach { nonce ->
                                val nonceHex = makeNonceHex(nonce)
                                val problem = "$prevHex$nonceHex"
                                le("PrevHex: $prevHex")
                                le("Nonce: $nonceHex")
                                le("Problem: $problem")
                                val result = runHash(problem, target)
                                send(makeSubmitRequest(Util.bytesToHex(result), nonceHex))
                            }
                        }
                    } ?: let {
                        le("no param..? what the fuck!?")
                    }
                }

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
            bytesToHex(this)
        }
    }

    private fun runHash(problem: String, target: String = ""): ByteArray {
        val input = hexStringToByteArray("$problem$target")
        slowHash(input, ByteArray(0), 0)
        return output
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

    private fun makeSubmitRequest(hashHex: String, nonceHex: String) =
        JSONObject().apply {
            put("method", "mining.submit")
            put("id", "1")
            put("params", JSONObject()
                .put("id", "")
                .put("job_id", "0")
                .put("nonce", nonceHex)
                .put("result", hashHex))
        }

    private fun le(s: String) {
        Log.e("MainActivity", s)
    }

    /**
     * Native implementation of cryptonight hash function.
     */
    external fun slowHash(input: ByteArray, output: ByteArray, variant: Int)
}
