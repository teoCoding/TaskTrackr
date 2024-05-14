package com.teocoding.tasktrackr.presentation.screen.photo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.Photo

@Composable
fun PhotoDialog(
    photo: Photo,
    onDismissRequest: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {

        Surface(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {

                IconButton(onClick = onClickDelete, modifier = Modifier.align(Alignment.End)) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_24dp),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = photo.localPath,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }

        }

    }

}