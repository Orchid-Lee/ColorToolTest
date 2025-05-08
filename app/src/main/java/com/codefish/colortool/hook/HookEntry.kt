package com.codefish.colortool.hook

import android.os.Build
import android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM
import com.codefish.colortool.hook.android.corepatch.CorePatchForV
import com.codefish.colortool.hook.appdetail.appdetail
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.suqi8.oshin.hook.android.android
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

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
        loadApp(hooker = appdetail())
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                if (lpparam.packageName == "android" && lpparam.processName == "android") {
                    if (Build.VERSION.SDK_INT == VANILLA_ICE_CREAM) {
                        CorePatchForV()
                            .handleLoadPackage(lpparam)
                    }
                }
            }
        }
        YukiXposedEvent.onInitZygote { startupParam: IXposedHookZygoteInit.StartupParam ->
            run {
            }
        }
    }
}