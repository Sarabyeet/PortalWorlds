package com.sarabyeet.portalworlds.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.arch.SharedViewModel
import com.squareup.picasso.Picasso

class PortalWorldsActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portal_worlds)

        // Firebase crashlytics - setting an user id, useful in authentication case
        FirebaseCrashlytics.getInstance().setUserId("1245")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.charactersListFragment,
                R.id.episodesListFragment,
                R.id.searchCharacterFragment
            ),
            drawerLayout
        )
        val navView= findViewById<NavigationView>(R.id.nav_view)

        // region Header image for drawer layout
        val hView = navView.getHeaderView(0)
        val image = hView.findViewById<ImageView>(R.id.drawer_image_view)

        viewModel.randomCharacter()
        viewModel.getRandomCharacterLiveData.observe(this){ character ->
            character?.let {
                Picasso.get().load(it.image).placeholder(R.drawable.progress_animation).into(image)
                hView.findViewById<TextView>(R.id.drawer_text_view).text = character.name
            }
        }
        // endregion Header image for drawer layout

        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )

        navView
            .setupWithNavController(navController)

        // To check the drawer layout item
        navView
            .setCheckedItem(
                navController.graph.startDestinationId
            )
    }

    override fun onResume() {
        super.onResume()
        viewModel.randomCharacter()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}