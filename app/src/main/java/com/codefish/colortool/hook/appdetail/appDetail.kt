package com.codefish.colortool.hook.appdetail

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Modifier

class appDetail: YukiBaseHooker() {
    override fun onHook() {
        //频繁安装检测
        var installationFrequencyMethodName = ""
        //风险检测
        val attemptInstallationMethod = ""
        var securityCheckMethod = ""
        DexKitBridge.create(this.appInfo.sourceDir).use {
            installationFrequencyMethodName = it.findMethod {
                searchPackages("com.oplus.appdetail.model.entrance")
                matcher {
                    modifiers = Modifier.PRIVATE
                    paramTypes = listOf<String>()
                    returnType("void")
                    usingStrings("1")
                }
            }.singleOrNull()?.methodName.toString()
            it.findClass {
                searchPackages("com.oplus.appdetail.model.guide.viewModel")
                matcher {
                    source("GuideShareViewModel.kt")
                }
            }.also {
                securityCheckMethod = it.findMethod {
                    matcher {
                        modifiers = Modifier.PUBLIC
                        paramTypes = listOf<String>()
                        returnType("boolean")
                        usingNumbers(0)
                        invokeMethods {
                            add {
                                name = "getPackageUri"
                            }
                        }
                    }
                }.singleOrNull()?.methodName.toString()
            }
        }
        //安装频繁
        "com.oplus.appdetail.model.entrance.ChannelBarrageActivity".toClass().apply {
            method {
                name = installationFrequencyMethodName
                emptyParam()
                returnType = UnitType
            }.hook {
                replaceUnit {
                    method {
                        name = attemptInstallationMethod
                        emptyParam()
                        returnType = UnitType
                    }.get(instance).call()
                }
            }
        }

        //移除安装前安全检测
        "com.oplus.appdetail.model.guide.viewModel.GuideShareViewModel".toClass().apply {
            method {
                name = securityCheckMethod
                emptyParam()
                returnType = BooleanType
            }.hook {
                before {
                    result = true
                }
            }
        }

        XposedBridge.log("ColorTool: 系统安装器重启成功...")
    }
}
