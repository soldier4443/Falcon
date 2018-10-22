package com.turastory.shanghycon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*


/**
 * Created by tura on 2018-09-15.
 */

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        showFeed()

        bottomNavigation.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.action_feed) {
                showFeed()
            } else if (it.itemId == R.id.action_wallet) {
                showWallet()
            }
            true
        }
    }

    private fun showWallet() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, WalletFragment(), "wallet")
            .commit()
    }

    private fun showFeed() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FeedFragment(), "feed")
            .commit()
    }
}