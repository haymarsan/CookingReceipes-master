package com.hms.cookingreceipes

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.hms.cookingreceipes.data.model.AppUpdate
import com.hms.cookingreceipes.databinding.DialogFragmentAppUpdateBinding

private const val ARG_APP_UPDATE = "argument_app_update_data"

class AppUpdateDialogFragment : androidx.fragment.app.DialogFragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ivClose: ImageView
    private lateinit var btnPlayStore: TextView
    private lateinit var btnDirectLink: TextView

    private var _binding: DialogFragmentAppUpdateBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(appUpdate: AppUpdate) =
            AppUpdateDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_APP_UPDATE, appUpdate)
                }
            }
    }

    private var mListener: ActionListener? = null

    private lateinit var appUpdate: AppUpdate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appUpdate = it.getSerializable(ARG_APP_UPDATE) as AppUpdate
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogFragmentAppUpdateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tvTitle = binding.tvTitle
        tvDescription = binding.tvDescription
        ivClose = binding.ivClose
        btnDirectLink = binding.btnDirectLink
        btnPlayStore = binding.btnPlayStore

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        tvTitle.text = appUpdate.title
        tvDescription.text = appUpdate.instruction

        if (appUpdate.isForcedUpdate) {
            isCancelable = false
            ivClose.visibility = View.GONE
        }
        if (appUpdate.isPlaystoreAvailable) {
            btnPlayStore.visibility = View.VISIBLE
            btnDirectLink.visibility = View.GONE
        } else {
            btnPlayStore.visibility = View.GONE
            btnDirectLink.visibility = View.VISIBLE
        }
        ivClose.setOnClickListener {
            dismiss()
        }
        btnPlayStore.setOnClickListener {
            mListener?.let {
                it.onPlayStoreClicked()
                dismiss()
            }
        }
        btnDirectLink.setOnClickListener {
            mListener?.let {
                it.onDirectDownloadClicked()
                dismiss()
            }
        }
        return root
    }

    fun setListener(listener: ActionListener) {
        mListener = listener
    }

    interface ActionListener {
        fun onPlayStoreClicked()
        fun onDirectDownloadClicked()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}