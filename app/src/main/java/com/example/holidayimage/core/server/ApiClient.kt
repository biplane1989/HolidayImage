package kotlincodes.com.retrofitwithkotlin.retrofit

import android.os.Build
import com.example.holidayimage.core.server.tls.TLSSocketFactory
import com.example.holidayimage.utils.Constance
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException


object ApiClient {
    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder().setLenient().create()

            var client: OkHttpClient? = null
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                try {
                    val tlsSocketFactory = TLSSocketFactory()
                    client = if (tlsSocketFactory.trustManager != null) {
                        OkHttpClient.Builder().sslSocketFactory(tlsSocketFactory , tlsSocketFactory.trustManager).build()
                    } else {
                        OkHttpClient.Builder().sslSocketFactory(tlsSocketFactory).build()
                    }
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                }
            } else {
                client = OkHttpClient.Builder().build()
            }

            val retrofit = Retrofit.Builder().baseUrl(Constance.BASE_URL)
                .client(client!!)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

            return retrofit.create(ApiInterface::class.java)
        }
}