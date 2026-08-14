package pl.wluczak.myexpenses

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.wluczak.myexpenses.di.AppModule

class MyExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyExpensesApplication)
            modules(AppModule)
        }
    }
}