package com.xu.kotandroid.aop

/**
 * @Author Xu
 * Date：2023/3/1 10:29
 * Description：
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Permissions constructor(
    /**
     * 需要申请权限的集合
     */
    vararg val value: String
)