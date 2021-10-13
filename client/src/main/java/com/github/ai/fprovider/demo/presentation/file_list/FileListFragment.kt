package com.github.ai.fprovider.demo.presentation.file_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import com.github.ai.fprovider.demo.R
import com.github.ai.fprovider.demo.data.entity.FileEntity
import com.github.ai.fprovider.demo.extension.setupActionBar
import com.github.ai.fprovider.demo.extension.showToastMessage
import com.github.ai.fprovider.demo.extension.toPath
import com.github.ai.fprovider.demo.extension.toUri
import com.github.ai.fprovider.demo.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class FileListFragment : Fragment() {

    private val viewModel: FileListViewModel by viewModel()
    private var backCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.base_fragment, container, false)

        val composeView: ComposeView = view.findViewById(R.id.composeView)

        composeView.apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FileListScreen(viewModel = viewModel)
            }
        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        backCallback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackClicked()
        }
    }

    override fun onStop() {
        super.onStop()
        backCallback?.remove()
        backCallback = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToLiveData()
        subscribeToEvents()

        viewModel.loadData()
    }

    private fun subscribeToLiveData() {
        viewModel.actionBarTitle.observe(viewLifecycleOwner) {
            setActionBarTitle(it)
        }
        viewModel.isActionBarBackButtonVisible.observe(viewLifecycleOwner) {
            setBackButtonVisible(it)
        }
    }

    private fun subscribeToEvents() {
        viewModel.showToastMessageEvent.observe(viewLifecycleOwner, EventObserver {
            showToastMessage(it)
        })
        viewModel.openFileEvent.observe(viewLifecycleOwner, EventObserver {
            openFile(it)
        })
    }

    private fun setActionBarTitle(text: String) {
        setupActionBar {
            title = text
        }
    }

    private fun setBackButtonVisible(isBackVisible: Boolean) {
        setupActionBar {
            setHomeAsUpIndicator(null)
            setDisplayHomeAsUpEnabled(isBackVisible)
        }
    }

    private fun openFile(file: FileEntity) {
        val uri = file.toPath().toUri()

        val intent = Intent(Intent.ACTION_VIEW)
            .apply {
                if (!file.mimeType.isNullOrBlank()) {
                    setDataAndType(uri, file.mimeType)
                } else {
                    data = uri
                }
            }
        startActivity(intent)
    }

    companion object {
        fun newInstance(): FileListFragment = FileListFragment()
    }
}