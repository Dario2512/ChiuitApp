package upt.ac.lab2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import upt.ac.lab2.ComposeActivity.Companion.EXTRA_TEXT
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val chiuitText = mutableStateOf("")

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                extractText(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chiuitText.value = getText(R.string.chiuit_example).toString()
        setContent { HomeScreen() }

    }

    @Composable
    private fun HomeScreen() {

        val text by remember { chiuitText }

        Surface(color = Color.White) {
            Box(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(8.dp),
                            text = text,
                        )
                        Button(
                            modifier = Modifier
                                .weight(0.2f)
                                .padding(8.dp),
                            onClick = { shareChiuit(text) }) {
                            Icon(
                                Icons.Filled.Send,
                                stringResource(R.string.send_action_icon_content_description)
                            )
                        }
                    }
                }
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = { composeChiuit(text) },
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(R.string.edit_action_icon_content_description)
                    )
                }
            }
        }
    }

    /*
    Defines text sharing/sending *implicit* intent, opens the application chooser menu,
    and starts a new activity which supports sharing/sending text.
     */
    private fun shareChiuit(text: String) {
        val sendIntent = Intent().apply {
            // TODO 1: Configure to support text sending/sharing and then attach the text as intent's extra.
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val intentChooser = Intent.createChooser(sendIntent, "")
        startActivity(intentChooser)
    }

    /*
    Defines an *explicit* intent which will be used to start ComposeActivity.
     */
    private fun composeChiuit(text: String) {
        val sendIntent = Intent(this, ComposeActivity::class.java).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        // We start a new activity that we expect to return the acquired text as the result.
        this.resultLauncher.launch(sendIntent)
    }

    private fun extractText(data: Intent?) {
        data?.let {
            val ss: String = data?.getStringExtra(Intent.EXTRA_TEXT).toString()

            if (!ss.isNullOrEmpty()) {
                chiuitText.value = ss
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        HomeScreen()
    }
}
