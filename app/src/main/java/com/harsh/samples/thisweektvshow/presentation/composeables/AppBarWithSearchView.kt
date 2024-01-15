package com.harsh.samples.thisweektvshow.presentation.composeables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBarWithSearchView(
    titleText: String,
    actions: @Composable RowScope.() -> Unit = {},
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchIconClick: () -> Unit = {},
    onSearchImeClicked: () -> Unit,
    onCloseClicked: () -> Unit = {}
) {
    var isSearchOpen by remember { mutableStateOf(false) }
    if (isSearchOpen) {
        SearchAppBar(
            text = searchText,
            onTextChange = onSearchTextChange,
            onCloseClicked = {
                isSearchOpen = false
                onCloseClicked()
            },
            onSearchImeClicked = {
                isSearchOpen = false
                onSearchImeClicked()
            }
        )
    } else {
        AppBar(
            title = titleText,
            actions = actions,
            onSearchIconClicked = {
                isSearchOpen = true
                onSearchIconClick()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit,
    onSearchIconClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(onClick = { onSearchIconClicked() }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            actions()
        }
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchImeClicked: (String) -> Unit
) {

    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = "Search here...",
                    modifier = Modifier.alpha(0.6f)
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { onSearchImeClicked(text) },
                    modifier = Modifier.alpha(0.6f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchImeClicked(text)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
