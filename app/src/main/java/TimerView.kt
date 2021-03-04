import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

@Composable
fun TimerView(timeLeftInSeconds: MutableStateFlow<Long>) {
    val totalSeconds = 60F
    val timeLeftInSecondsState = timeLeftInSeconds.collectAsState()
    val timeLeft = timeLeftInSecondsState.value / totalSeconds
    Box(modifier = Modifier.padding(bottom = 350.dp), contentAlignment = Alignment.Center) {
        ProgressDial(timeLeft)
        TimerCountdown(timeLeftInSecondsState.value.toLong())
    }
}

@Composable
fun ProgressDial(timeLeft: Float) {

    Log.v(TAG, "progress bar $timeLeft")

    Box(
        modifier = Modifier
            .padding(7.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularProgressIndicator(
            progress = 1.0F,
            strokeWidth = 25.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            color = Color.Gray
        )
        CircularProgressIndicator(
            progress = timeLeft, // 0.0 to 1.0
            strokeWidth = 25.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            color = // if (timeDisplay > .25) Color.Green else Color.Red //if(a < b) c else d
            when {
                timeLeft < .10 -> Color.Red
                timeLeft < .30 -> Color.Yellow
                else ->
                    Color.Green
            }
        )
    }
}

@Composable
fun TimerCountdown(timeLeft: Long) {
    // val time = timeLeft.collectAsState().value.toFloat()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            getFormattedStopWatchTime(timeLeft),
            style = MaterialTheme.typography.h4
        )
    }
}

fun getFormattedStopWatchTime(sec: Long): String {
    var seconds = sec
    // Convert to hours
    val hours = TimeUnit.SECONDS.toHours(seconds)
    seconds -= TimeUnit.HOURS.toSeconds(hours)

    // Convert to minutes
    val minutes = TimeUnit.SECONDS.toMinutes(seconds)
    seconds -= TimeUnit.MINUTES.toSeconds(minutes)

    val time = "${if (hours <10) "0" else ""}$hours:" +
            "${if (minutes < 10) "0" else ""}$minutes:" +
            "${if (seconds < 10) "0" else ""}$seconds"

    return time
}