package com.common.typealiases

import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import java.util.*

typealias GenericEvent = () -> Unit
typealias OnCodeCompleteListener = (text: String) -> Unit
typealias OnCodeChangeListener = (text: String) -> Unit
typealias OnAdapterChange = (values: List<Any>) -> Unit
typealias OnClickItem = (v: View) -> Unit
typealias OnTextChange = (text: String?) -> Unit
typealias OnTextChanged = (value: CharSequence?, start: Int, before: Int, count: Int) -> Unit
typealias BeforeTextChanged = (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit
typealias AfterTextChanged = (s: Editable?) -> Unit
typealias OnEditorActionListener = (view: TextView, actionId: Int, event: KeyEvent) -> Boolean
typealias OnTextChangedWithMask = (value: String) -> Unit
typealias OnCancelDialog = () -> Unit
typealias OnOkDialog = (dateSelected: Calendar?) -> Unit
typealias OnHideValueComponentFlagChange = (flag: Boolean) -> Unit
//remover unit and put a generic message
typealias OnErrorBaseViewModel = suspend (
    statusCode: Int?,
    error: String,
    throwable: Throwable?
) -> Unit
typealias OnSuccessBaseViewModel<T> = suspend (value: T) -> Unit
typealias OnCompletionBaseViewModel = suspend () -> Unit