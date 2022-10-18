package com.xu.kotandroid.util.cor

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

/**
 * @Author Xu
 * Date：2022/10/14 10:15
 * Description：协程作用域
 */
interface CoroutineScope {
    val scopeContext: CoroutineContext
}

fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {

    val completion = Continuation<Unit>(context) {

    }
    block.startCoroutine(this, completion)

}