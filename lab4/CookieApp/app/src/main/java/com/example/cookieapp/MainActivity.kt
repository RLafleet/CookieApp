import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cookieapp.ClickerViewModel
import com.example.cookieapp.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: ClickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.toastFlow
                .collect { toastMessage ->
                    showToast(toastMessage)
                }
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
