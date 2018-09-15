package com.turastory.shanghycon

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turastory.shanghycon.network.hycon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_wallet.*

/**
 * Created by tura on 2018-09-15.
 */
class WalletFragment : Fragment() {
    companion object {
        const val address = "H2UevVaqYWo3Quw9oKPqU632VwP9oCcKB"
    }

    var recent: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressValue.text = address

        hycon.getBalance()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {

            }
            .subscribe { accountBalance ->
                accountBalance?.balance?.let { it ->
                    recent = it
                    hyconValue.text = "$it HYC"
                } ?: let {
                    hyconValue.text = "$recent HYC"
                }
            }
    }
}