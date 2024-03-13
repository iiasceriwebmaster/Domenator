package md.webmasterstudio.domenator.networking;

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.model.DictionaryModel

/* loaded from: classes.dex */
class CheckInternetConnection(private val context: Context) : AppCompatActivity() {
    fun isOnline(activity: Activity): Boolean {
        val activeNetworkInfo =
            (activity.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun showNoInternetConnectionAlertDialog(activity: Activity, dictionaryModel: DictionaryModel) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("dictionaryModel.getMSG_ALL_INTERNET_CONNECTION_DIALOG_TITLE()")
        builder.setIcon(R.drawable.ic_error_outline_red_24dp)
        builder.setMessage("dictionaryModel.getMSG_ALL_INTERNET_CONNECTION_DIALOG_MESSAGE()")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "dictionaryModel.getMSG_ALL_INTERNET_CONNECTION_DIALOG_BUTTON()"
        ) { _, _ ->
            activity.recreate()
        }
        val create = builder.create()
        create.create()
        create.show()
        val layoutParams = LinearLayout.LayoutParams(-1, -2)
        layoutParams.weight = 1.0f
        layoutParams.gravity = 17
        create.getButton(-1).layoutParams = layoutParams
    }

    fun showDefaultNoInternetConnectionAlertDialog(activity: Activity) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(context.getString(R.string.dlg_no_internet_connection_title))
        builder.setIcon(R.drawable.ic_error_outline_red_24dp)
        builder.setMessage(context.getString(R.string.dlg_no_internet_connection_message))
        builder.setCancelable(false)
        builder.setPositiveButton(
            context.getString(R.string.dlg_no_internet_connection_try_again_button)
        ) { _, _ ->
            activity.recreate()
        }
        val create = builder.create()
        create.create()
        create.show()
        val layoutParams = LinearLayout.LayoutParams(-1, -2)
        layoutParams.weight = 1.0f
        layoutParams.gravity = 17
        create.getButton(-1).layoutParams = layoutParams
    }
}
