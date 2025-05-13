package com.codefish.colortool.hook

import com.codefish.colortool.hook.appdetail.appDetail
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.suqi8.oshin.hook.android.android

@InjectYukiHookWithXposed(entryClassName = "ColorTool")
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "ColorTool"
        }
        isDebug = false
    }

    override fun onHook() = encase {
        // Your code here.
        System.loadLibrary("dexkit")
        loadApp(hooker = android())
        loadApp(hooker = appDetail())
    }
}