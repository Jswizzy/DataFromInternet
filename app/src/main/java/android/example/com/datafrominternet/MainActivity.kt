package android.example.com.datafrominternet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.example.com.datafrominternet.utilities.NetworkUtils
import android.os.AsyncTask
import android.view.View
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val mSearchResult by lazy { tv_github_search_results_json }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    inner class GithubQueryTask: AsyncTask<URL, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            pb_loading_indicator.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg urls: URL?): String {

            var responseFromHttpUrl: String? = null
            try {
                responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(urls[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return responseFromHttpUrl ?: ""
        }

        override fun onPostExecute(result: String) {
            pb_loading_indicator.visibility = View.INVISIBLE

            if (result != "") {
                showJsonDataView()
                mSearchResult.text = result
            } else {
                showErrorMessage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val menuItemThatWasSelected = item?.itemId

        if (menuItemThatWasSelected == R.id.action_search) {
            val message = "Search Clicked"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            makeGithubSearchQuery()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun makeGithubSearchQuery() {
        val githubQuery = et_search_box.text.toString()
        val githubSearchUrl = NetworkUtils.buildUrl(githubQuery)
        tv_url_display.text = githubSearchUrl.toString()

        GithubQueryTask().execute(githubSearchUrl)
    }

    private fun showJsonDataView() {
        tv_error_message_display.visibility = View.INVISIBLE
        tv_github_search_results_json.visibility = View.VISIBLE
    }

    private fun showErrorMessage() {
        tv_error_message_display.visibility = View.VISIBLE
        tv_github_search_results_json.visibility = View.INVISIBLE
    }
}
