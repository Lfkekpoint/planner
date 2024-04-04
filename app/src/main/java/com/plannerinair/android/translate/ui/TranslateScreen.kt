package com.plannerinair.android.translate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.plannerinair.android.R
import com.plannerinair.android.translate.logic.TranslateFeature
import com.plannerinair.android.translate.logic.TranslateFeature.State.SelectedLanguage

@Composable
fun TranslateScreen(
    translateState: TranslateFeature.State,
    listener: (TranslateFeature.Msg) -> Unit
) = Column(verticalArrangement = Arrangement.Bottom) {
    if (translateState.errorMessage == null) {
        Text(translateState.translatedText)
    } else {
        Text(translateState.errorMessage)
    }
    TextField(value = translateState.inputText, onValueChange = { listener(TranslateFeature.Msg.OnTextInput(it)) } )
    Row() {
        val languages = translateState.languagesState.availableLanguages.map { it.displayName }
        val selectedFrom = when (val lang = translateState.languagesState.selectedFrom) {
            is SelectedLanguage.Auto -> "Auto"
            is SelectedLanguage.Concrete -> lang.language.displayName
        }
//        languagesSpinner(selectedFrom, listOf("Auto") + languages) { item ->
//            val lang = translateState.languagesState.availableLanguages.find {
//                it.displayName == item
//            }
//            listener(TranslateFeature.Msg.OnLanguageFromChange(lang))
//        }
        Button(onClick = { listener(TranslateFeature.Msg.OnLanguageSwapClick) }) {
            Icon(painterResource(id = R.drawable.ic_interchange_24), contentDescription = null)
        }
        val selectedTo = translateState.languagesState.selectedTo.displayName
//        languagesSpinner(selectedTo, languages) { item ->
//            val lang = translateState.languagesState.availableLanguages.find {
//                it.displayName == item
//            }
//            lang?.let { listener(TranslateFeature.Msg.OnLanguageToChange(it)) }
//        }
    }
}

//private fun languagesSpinner(
//    selected: String,
//    languages: List<String>,
//    listener: (String) -> Unit
//) {
//    appCompatSpinner {
//        init { v ->
//            v as Spinner
//            v.adapter = ArrayAdapter(
//                v.context,
//                android.R.layout.simple_spinner_item,
//                languages
//            )
//            v.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapterView: AdapterView<*>,
//                    view: View?,
//                    i: Int,
//                    l: Long
//                ) {
//                    val item = adapterView.getItemAtPosition(i).toString()
//                    listener(item)
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {}
//            }
//        }
//        selection(languages.indexOf(selected))
//    }
//}
//
