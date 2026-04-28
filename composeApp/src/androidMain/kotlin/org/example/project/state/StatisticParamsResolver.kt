package org.example.project.state

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.example.project.model.StatisticParameter
import org.example.project.utils.getDeviceString
import org.example.project.utils.getGadid
import org.example.project.utils.getRef
import org.example.project.utils.runProbe

class StatisticParamsResolver(
    private val context: Context
) {

    suspend fun resolveAll(): List<StatisticParameter> = coroutineScope {

        val result = listOf(

            async {
                val value = getRef(context)
//                log("Param: referrer = $value")
                StatisticParameter("slgj2m618", value)
            },

            async {
                val value = getGadid(context)
//                log("Param: gadid = $value")
                StatisticParameter("wf2rsv", value)
            },

            async {
                val value = runProbe(context).toString()
//                val value = "0"
//                log("Param: probe = $value")
                StatisticParameter("dip71h", value)
            },

            async {
                val value = getDeviceString()
//                log("Param: device = $value")
                StatisticParameter("ezv88", value)
            },

            async {
                val value = runCatching {
                    Firebase.analytics.appInstanceId.await()
                }.getOrNull()

//                log("Param: externalId = $value")
                StatisticParameter("xg0v2", value)
            },

            async {
                val value = runCatching {
                    val pi = context.packageManager
                        .getPackageInfo(context.packageName, 0)
                    pi.firstInstallTime.toString()
                }.getOrNull()

//                log("Param: install_time = $value")
                StatisticParameter("xwj0viupee", value)
            }

        ).awaitAll()

        result
    }
}