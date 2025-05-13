package com.codefish.colortool

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.codefish.colortool.databinding.ActivityMainBinding
import com.highcapable.yukihookapi.YukiHookAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        var isGiveRoot: Int

        try {
            val process = Runtime.getRuntime().exec("su -c cat /system/build.prop")
            isGiveRoot = process.waitFor()
        } catch (e: Exception) {
            isGiveRoot = 3
        }

        if (isGiveRoot == 0){
            binding.validStatus.text = "${binding.validStatus.text}, 模块已Root"
        }else{
            binding.validStatus.text = "${binding.validStatus.text}, 模块未Root"
        }

        binding.apply {
            reload.setOnClickListener{
                restartApp("com.oplus.appdetail")
                toast(R.string.reload_success)
            }
        }
    }

    private fun restartApp(packageName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val command = "pkill -f " + packageName
                // 使用 su 执行命令
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))

                // 等待命令执行完成
                process.waitFor()
            } catch (e: Exception) {
                e.printStackTrace()
                // 处理错误
            }
        }
    }

}