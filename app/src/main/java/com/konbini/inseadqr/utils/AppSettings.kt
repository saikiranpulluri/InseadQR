package com.konbini.inseadqr.utils

import android.util.Log

object AppSettings {
    private const val nameSpace = "com.konbini.inseadqr"

    fun getAllSetting() {
        getChildClasses(this.javaClass.declaredClasses)
    }

    private fun getChildClasses(classes: Array<Class<*>?>?) {
        if (classes != null) {
            if (classes.isNotEmpty()) {
                classes.forEach { cl ->
                    val childCl = cl?.declaredClasses
                    if (cl?.declaredFields?.isNotEmpty() == true) {
                        val className = cl.name.replace("$", ".")

                        cl.declaredFields.forEach { field ->
                            val fieldName = field.name
                            val instance = cl.declaredFields[0]

                            if (!fieldName.endsWith("INSTANCE")) {
                                field.isAccessible = true
                                var value: Any? = null
                                val spKey = "$className.$fieldName".replace("$nameSpace.", "")
                                //val spKey = fieldName.toString()
                                val fieldType = field.type.name

                                // Get SP value, if not found return default value of property
                                when (fieldType) {
                                    "java.lang.String" -> {
                                        val stringValue = field.get(instance)?.toString()
                                        value = PrefUtil.getString(spKey, stringValue ?: "")
                                        field.set(instance, value)
                                    }
                                    "int" -> {
                                        val intValue = field.getInt(instance)
                                        val spValue = PrefUtil.getInt(spKey, intValue)
                                        value = spValue
                                        field.setInt(instance, spValue)
                                    }
                                    "long" -> {
                                        val longValue = field.getLong(instance)
                                        val spValue = PrefUtil.getLong(spKey, longValue)
                                        value = spValue
                                        field.setLong(instance, spValue)
                                    }
                                    "boolean" -> {
                                        val boolValue = field.getBoolean(instance)
                                        val spValue = PrefUtil.getBoolean(spKey, boolValue)
                                        value = spValue
                                        field.setBoolean(instance, spValue)
                                    }
                                }

                                Log.d("TEST", "$spKey ($fieldType) = $value")
                            }
                        }
                    }
                    getChildClasses(childCl)
                }
            }
        }
    }

    object Cloud {
        var Host = "https://dev.ineedfood.today"
        var ClientId = "6jTgykTObi6oSBXIb3OefFYt7Zw6YRarZq25qkpna"
        var ClientSecret = "mVV2qow4sIBPKOTIAKRuCQ9e7ksvJ0EQ8SGKAfj8"
    }

/*    object Wallet {
        var Host = "https://stamford.ineedfood.today"
        var ClientId = "yvJ2z4hTmpDTM2NKPwAtMzJ9SBlU6w9Q202uk1Da"
        var ClientSecret = "2zJyJxORvfiu5u7lpyDx9Te4E04MWwdG9g5F0zDO"
    }*/

    object APIs {
        var UseNativeWoo = false
        var ListCustomerOrders = "/wp-json/wc/v2/orders"
        var EnableTemporaryLogin = "/wp-json/wp/v2/temporary-login/enable-temporary-login"
        var DisableTemporaryLogin = "/wp-json/wp/v2/temporary-login/disable-temporary-login"
    }
}