package com.codefish.colortool

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.codefish.colortool.databinding.ActivityMainBinding
import com.highcapable.yukihookapi.YukiHookAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

open class MainActivity : Activity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (YukiHookAPI.Status.isModuleActive){
            binding.validStatus.text = "模块已激活"
        }else{
            binding.validStatus.text = "模块未激活"
        }

        val process = Runtime.getRuntime().exec("su -c cat /system/build.prop")
        var isGiveRoot = process.waitFor()
    }
}

suspend fun executeCommand(command: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            process.waitFor()
            reader.close()
            output.toString().trim()
        } catch (e: Exception) {
            Log.e("ColorTool", "executeCommand: $e")
            return@withContext "0"
        }
    }
}