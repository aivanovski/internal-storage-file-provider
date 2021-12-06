package com.github.ai.isfprovider.domain

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MetaDataReaderTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .get() as Context
    }

    @Test
    fun `readAuthority should read authority from test AndroidManifest`() {
        val authority = MetaDataReader().readAuthority(context)
        assertThat(authority).isEqualTo("test-authority")
    }
}